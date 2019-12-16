package main;
import entidades.Acceso;
import entidades.URL;
import entidades.Usuario;
import freemarker.template.Configuration;
import org.jasypt.util.text.StrongTextEncryptor;
import services.AccesoService;
import services.DataBaseService;
import services.URLService;
import services.UsuarioService;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import ua_parser.Client;
import ua_parser.Parser;
import utils.Encoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static spark.Spark.*;
public class Main {
    private static String encriptorKey = "aHaf920@_9";

    public static void main(String[] args){
        Spark.port(getHerokuAssignedPort());
        Spark.staticFileLocation("/public");
        /*Inicializacion de la base de datos.*/
        DataBaseService.getInstance().init();
        createEntities();
        /*Configuraciones del motor de plantillas.*/
        staticFiles.location("/public");
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setClassForTemplateLoading(Main.class, "/public/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        before("*", (request, response) -> {
            Session session = request.session(true);
            if(session.attribute("usuario") == null)
                session.attribute("usuario", "");
        });

        before("/admin", (request, response) -> {
            if(request.cookie("username") == null)
                response.redirect("/login");
            return;
        });

        Spark.get("/", (request, response) -> {
            response.redirect("/home");
            return "";
        });

        Spark.get("/hacerLogout", (request, response) -> {
            //creando cookie en para un minuto
            Session session = request.session();
            session.invalidate();
            response.removeCookie("/", "username");
            response.removeCookie("/", "nombre");
            response.removeCookie("/", "apellido");
            response.removeCookie("/", "password");
            response.removeCookie("/", "isadmin");
            response.removeCookie("/", "url_referencia");
            response.redirect("/home");
            return "";
        });

        Spark.get("/home", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Home");
            List<Usuario> usuarios = UsuarioService.getInstance().findAll();
            Usuario usuario = getCookieUser(attributes, request);
            String urlReferencia = request.cookie("url_referencia");
            if(urlReferencia == null){
                attributes.put("urlreferencia", "");
            }else{
                String urlGenerado = URLService.getInstance().selectUrlByUrlReferenciaAndUsuario(urlReferencia, usuario)
                        .geturlGenerada();
                attributes.put("urlreferencia", urlReferencia);
                attributes.put("urlgenerado", urlGenerado);
            }
            attributes.put("articulos", usuarios);
            encriptingCookies(request, attributes);
            return new ModelAndView(attributes, "home.ftl");
        }, freeMarkerEngine);

        Spark.get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Log In");
            response.removeCookie("/", "url_referencia");
            return new ModelAndView(attributes, "login.ftl");
        }, freeMarkerEngine);

        Spark.get("/register", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Register");
            response.removeCookie("/", "url_referencia");
            return new ModelAndView(attributes, "register.ftl");
        }, freeMarkerEngine);

        Spark.post("/hacerLogin/", (request, response) -> {
            Session session = request.session(true);
            List<Usuario> usuarios = UsuarioService.getInstance().findAll();
            Usuario usuario = null;
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            for (Usuario usr : usuarios) {
                System.out.println(usr.toString());
                if (username.equalsIgnoreCase(usr.getUsername()) && password.equals(usr.getPassword())) {
                    usuario = new Usuario(usr.getUsername(), usr.getNombre(), usr.getApellido(), usr.getPassword(), usr.isAdministrator());
                    int recordar = (request.queryParams("recordar") != null ? 86400 : 1000);
                    StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
                    textEncryptor.setPassword(encriptorKey);
                    String encriptedUsername = textEncryptor.encrypt(usuario.getUsername());
                    String encriptedPassword = textEncryptor.encrypt(usuario.getPassword());
                    String encriptedName = textEncryptor.encrypt(usuario.getNombre());
                    String encriptedLastName = textEncryptor.encrypt(usuario.getApellido());
                    String encriptedIsAdmin = textEncryptor.encrypt(String.valueOf(usuario.isAdministrator()));
                    response.cookie("/", "username", encriptedUsername, recordar, false);
                    response.cookie("/", "password", encriptedPassword, recordar, false);
                    response.cookie("/", "nombre", encriptedName, recordar, false);
                    response.cookie("/", "apellido", encriptedLastName, recordar, false);
                    response.cookie("/", "isadmin", encriptedIsAdmin, recordar, false);
                    response.redirect("/home");
                }
            }
            session.attribute("usuario", usuario);
            //redireccionado a la otra URL.
            response.redirect("/login");
            return "";
        });

        Spark.post("/hacerRegister/", (request, response) -> {
            String nombre = request.queryParams("nombre");
            String apellido = request.queryParams("apellido");
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            boolean isadmin = false;
            String auxIsAdmin = request.queryParams("isadmin");
            if(auxIsAdmin != null && auxIsAdmin.equals("on")){
                isadmin = true;
            }
            System.out.println(request.queryParams("isauthor"));
            Usuario usuario = new Usuario(username, nombre, apellido, password, isadmin);
            UsuarioService.getInstance().crear(usuario);

            //redireccionado a la otra URL.
            response.removeCookie("url_referencia");
            response.redirect("/login");

            return "";
        });

        Spark.post("/eliminarURL/:id", (request, response) -> {
            long id = Long.parseLong(request.params("id"));
            URLService.getInstance().eliminar(id);
            response.redirect("/admin");
            return "";
        });

        Spark.post("/generarURL", (request, response) -> {
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(encriptorKey);
            String urlReferencia = request.queryParams("urlReferencia");
            Usuario guess = UsuarioService.getInstance().find("guess");
            String currentUserUsername;
            if(request.cookie("username") != null){
                currentUserUsername = textEncryptor.decrypt(request.cookie("username"));
            } else{
                currentUserUsername = guess.getUsername();
            }
            Usuario usuario;
            if(request.cookie("username") != null){
                usuario = new Usuario(
                        currentUserUsername,
                        textEncryptor.decrypt(request.cookie("nombre")),
                        textEncryptor.decrypt(request.cookie("apellido")),
                        textEncryptor.decrypt(request.cookie("password")),
                        Boolean.parseBoolean(textEncryptor.decrypt(request.cookie("isadmin"))));
            }else{
                usuario = guess;
                response.removeCookie("url_referencia");
                response.cookie("/", "url_referencia", urlReferencia, 60, false);
            }
            URLService.getInstance().generarURL(urlReferencia, usuario);
            if(request.cookie("username") != null){
                response.redirect("/admin");
            }else{
                response.redirect("/home");
            }
            return "";
        });

        Spark.get("homeWithoutURL", (request, response) -> {
            response.removeCookie("/", "url_referencia");
            response.redirect("/home");
            return "";
        });

        Spark.get("/admin", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();
            Usuario usuario = getCookieUser(attributes, request);
            if (usuario.getUsername().equalsIgnoreCase("admin")){
                response.redirect("/adminStatistics");
            }
            Usuario adminUser = new Usuario(
                    "admin",
                    "admin",
                    "admin",
                    "admin",
                    true
            );
            attributes.put("usuario", usuario);
            URL urlAux = new URL("\rd\bg", "https://www.amazon.com/", adminUser);
            ArrayList<URL> urlArrayList = new ArrayList<URL>();
            urlArrayList.add(urlAux);
            addingSoStatsUser(attributes, usuario.getUsername());
            addingHourStatsUser(attributes, usuario.getUsername());
            addingDayStatsUser(attributes, usuario.getUsername());
            addingBrowserStatsUser(attributes, usuario.getUsername());
            if(usuario != null){
                if(usuario.isAdministrator()){
                    attributes.put("links", URLService.getInstance().findAll());
                }else{
                    attributes.put("links", URLService.getInstance().selectUrlByUsername(usuario.getUsername()));
                }
            }else{
                attributes.put("links", new ArrayList<>());
            }
            return new ModelAndView(attributes, "panelAdmin.ftl");
        }, freeMarkerEngine);

        Spark.get("/homeLink", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();
            String username = request.session(true).attribute("usuario");
            Usuario usuario = UsuarioService.getInstance().find(username);
            attributes.put("usuario", usuario);
            return new ModelAndView(attributes, "homeLink.ftl");
        }, freeMarkerEngine);

        Spark.get("/adminStatistics", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();
            Usuario usuario = getCookieUser(attributes, request);
            attributes.put("usuario", usuario);
            addingGeneralHourStats(attributes);
            addingGeneralSoStats(attributes);
            addingGeneralDayStats(attributes);
            addingGeneralBrowserStats(attributes);
            return new ModelAndView(attributes, "adminStats.ftl");
        }, freeMarkerEngine);

        Spark.get("/adminUserControl", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();
            Usuario usuario = getCookieUser(attributes, request);
            List<Usuario> listaUsuarios = UsuarioService.getInstance().findAll();
            attributes.put("usuario", usuario);
            attributes.put("usuarios", listaUsuarios);
            return new ModelAndView(attributes, "adminUserControl.ftl");
        }, freeMarkerEngine);

        Spark.get("/rd/:code", (request, response) ->{
           String urlGenerada = request.pathInfo();
            System.out.println(urlGenerada);
            URL url = URLService.getInstance().selectUrlGenerada(urlGenerada);
            if (url != null){
                Parser parser = new Parser();
                Client client = parser.parse(request.userAgent());
                String ipCliente = request.ip();
                String navegador = client.userAgent.family;
                String sistemaOperativo = client.os.family;
                String deviceFamily = client.device.family;
                Date fechaHora = new Date(System.currentTimeMillis());
                //Anadiendo fecha y hora a la entidad de acceso para facilitar control. Borrame.
                long hour = LocalTime.now().getHour();
                String dia = LocalDate.now().getDayOfWeek().toString();
                System.out.println(sistemaOperativo);
                System.out.println("Para la url " + urlGenerada + " hubo una visita desde un: " + deviceFamily);
                //Creando registro de acceos.
                Acceso acceso = new Acceso(navegador, sistemaOperativo, ipCliente, fechaHora, url, hour, dia);
                AccesoService.getInstance().crear(acceso);
                //Métodos para actualizar la cantidad de accesos de una URL en especifico.
                long cantAccesos = AccesoService.getInstance().getCantAccesosByUrl(urlGenerada);
                url.setcantAccesos((int)cantAccesos);
                URLService.getInstance().editar(url);
                response.redirect(url.geturlReferencia());
            }else{
                /*Contenido sugerido...*/
                System.out.println("Going nowhere!");
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("loggedUser", request.session().attribute("usuario"));
    //            return getPlantilla(configuration, attributes, "notFound.ftl");
                response.redirect("/");
            }
            return "";
        });
        Spark.get("/generarReportes", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();
            String username = request.session(true).attribute("usuario");
            Usuario usuario = UsuarioService.getInstance().find(username);
            attributes.put("usuario", usuario);
            if(usuario != null){
                attributes.put("links", UsuarioService.getInstance().find(usuario).
                        getMisURLs());
            }else{
                attributes.put("links", new ArrayList<>());
            }
            return new ModelAndView(attributes, "panelAdmin.ftl");
        }, freeMarkerEngine);

        Spark.get("/campaignStatistics/:idCampaign", (request, response) ->{
            /*Todo: Haz que esta sección de Análisis individual funcione.*/
            Map<String, Object> attributes = new HashMap<>();
            String num = request.params("idCampaign");
            String idUrl = num.replaceAll(",", "");
            long urlId = Long.parseLong(idUrl);
            Usuario usuario = getCookieUser(attributes, request);
            List<Acceso> auxAccessList = AccesoService.getInstance().getAccesosByUrl(urlId);
            URL url = URLService.getInstance().find(urlId);
            attributes.put("usuario", usuario);
            attributes.put("accesos", auxAccessList);
            attributes.put("url", url);
            addingBrowserStats(attributes, urlId);
            addingDayStats(attributes, urlId);
            addingHourStats(attributes, urlId);
            addingSoStats(attributes, urlId);
//            if(usuario != null){
//                attributes.put("links", UsuarioService.getInstance().find(usuario).
//                        getMisURLs());
//            }else{
//                attributes.put("links", new ArrayList<>());
//            }
            return new ModelAndView(attributes, "campaign-statistics.ftl");
        }, freeMarkerEngine);

    }

    private static void addingSoStats(Map<String, Object> attributes, long urlId) {
        AccesoService aS = AccesoService.getInstance();
        long android = aS.getCantAccesosOs("Android", urlId);
        long iOs = aS.getCantAccesosOs("iOS", urlId);
        long macOs = aS.getCantAccesosOs("Mac OS", urlId);
        long linux = aS.getCantAccesosOs("Linux", urlId);
        long ubuntu = aS.getCantAccesosOs("Ubuntu", urlId);
        long windows = aS.getCantAccesosOs("Windows", urlId);

        attributes.put("and", android);
        attributes.put("io", iOs);
        attributes.put("mo", macOs);
        attributes.put("li", linux);
        attributes.put("ub", ubuntu);
        attributes.put("wi", windows);
    }

    private static void addingHourStats(Map<String, Object> attributes, long urlId) {
        AccesoService aS = AccesoService.getInstance();
        long zero = aS.getCantAccesosByHour( 0, urlId);
        long one = aS.getCantAccesosByHour( 1, urlId);
        long two = aS.getCantAccesosByHour( 2, urlId);
        long three = aS.getCantAccesosByHour( 3, urlId);
        long four = aS.getCantAccesosByHour( 4, urlId);
        long five = aS.getCantAccesosByHour( 5, urlId);
        long six = aS.getCantAccesosByHour( 6, urlId);
        long seven = aS.getCantAccesosByHour( 7, urlId);
        long eight = aS.getCantAccesosByHour( 8, urlId);
        long nine = aS.getCantAccesosByHour( 9, urlId);
        long ten = aS.getCantAccesosByHour( 10, urlId);
        long eleven = aS.getCantAccesosByHour( 11, urlId);
        long twelve = aS.getCantAccesosByHour( 12, urlId);
        long thirteen = aS.getCantAccesosByHour( 13, urlId);
        long fourteen = aS.getCantAccesosByHour( 14, urlId);
        long fifteen = aS.getCantAccesosByHour( 15, urlId);
        long sixteen = aS.getCantAccesosByHour( 16, urlId);
        long seventeen = aS.getCantAccesosByHour( 17, urlId);
        long eightteen = aS.getCantAccesosByHour( 18, urlId);
        long nineteen = aS.getCantAccesosByHour( 19, urlId);
        long twenty = aS.getCantAccesosByHour( 20, urlId);
        long twenty_one = aS.getCantAccesosByHour( 21, urlId);
        long twenty_two = aS.getCantAccesosByHour( 22, urlId);
        long twenty_three = aS.getCantAccesosByHour( 23, urlId);

        attributes.put("zero", zero);
        attributes.put("one", one);
        attributes.put("two", two);
        attributes.put("three", three);
        attributes.put("four", four);
        attributes.put("five", five);
        attributes.put("six", six);
        attributes.put("seven", seven);
        attributes.put("eight", eight);
        attributes.put("nine", nine);
        attributes.put("ten", ten);
        attributes.put("eleven", eleven);
        attributes.put("twelve", twelve);
        attributes.put("thirteen", thirteen);
        attributes.put("fourteen", fourteen);
        attributes.put("fifteen", fifteen);
        attributes.put("sixteen", sixteen);
        attributes.put("seventeen", seventeen);
        attributes.put("eighteen", eightteen);
        attributes.put("nineteen", nineteen);
        attributes.put("twenty", twenty);
        attributes.put("twenty_one", twenty_one);
        attributes.put("twenty_two", twenty_two);
        attributes.put("twenty_three", twenty_three);
    }

    private static void addingDayStats(Map<String, Object> attributes, long urlId) {
        AccesoService aS = AccesoService.getInstance();
        long cantMo = aS.getCantAccesosByDayOfWeek("MONDAY", urlId);
        long cantTue = aS.getCantAccesosByDayOfWeek("TUESDAY", urlId);
        long cantWen = aS.getCantAccesosByDayOfWeek("WEDNESDAY", urlId);
        long cantThu = aS.getCantAccesosByDayOfWeek("THURSDAY", urlId);
        long cantFri = aS.getCantAccesosByDayOfWeek("FRIDAY", urlId);
        long cantSat = aS.getCantAccesosByDayOfWeek("SATURDAY", urlId);
        long cantSun = aS.getCantAccesosByDayOfWeek("SUNDAY", urlId);

        attributes.put("mon", cantMo);
        attributes.put("tue", cantTue);
        attributes.put("wen", cantWen);
        attributes.put("thu", cantThu);
        attributes.put("fri", cantFri);
        attributes.put("sat", cantSat);
        attributes.put("sun", cantSun);
    }


    private static void addingBrowserStats(Map<String, Object> attributes, long urlId) {
        AccesoService aS = AccesoService.getInstance();
        long cantChrome = aS.getCantAccesosByBrowser("Chrome", urlId);
        long cantSafari = aS.getCantAccesosByBrowser("Safari", urlId);
        long cantOpera = aS.getCantAccesosByBrowser("Opera", urlId);
        long cantEdge = aS.getCantAccesosByBrowser("Edge", urlId);
        long cantFirefox = aS.getCantAccesosByBrowser("Firefox", urlId);
        long cantBrave = aS.getCantAccesosByBrowser("Brave", urlId);
        attributes.put("cc", cantChrome);
        attributes.put("cs", cantSafari);
        attributes.put("co", cantOpera);
        attributes.put("ce", cantEdge);
        attributes.put("cf", cantFirefox);
        attributes.put("cb", cantBrave);
    }
    private static void addingSoStatsUser(Map<String, Object> attributes, String username) {
        AccesoService aS = AccesoService.getInstance();
        long android = aS.getOsVisits("Android", username);
        long iOs = aS.getOsVisits("iOS", username);
        long macOs = aS.getOsVisits("Mac OS", username);
        long linux = aS.getOsVisits("Linux", username);
        long ubuntu = aS.getOsVisits("Ubuntu", username);
        long windows = aS.getOsVisits("Windows", username);

        attributes.put("and", android);
        attributes.put("io", iOs);
        attributes.put("mo", macOs);
        attributes.put("li", linux);
        attributes.put("ub", ubuntu);
        attributes.put("wi", windows);
    }

    private static void addingHourStatsUser(Map<String, Object> attributes, String username) {
        AccesoService aS = AccesoService.getInstance();
        long zero = aS.getHourlyVisits( 0, username);
        long one = aS.getHourlyVisits( 1, username);
        long two = aS.getHourlyVisits( 2, username);
        long three = aS.getHourlyVisits( 3, username);
        long four = aS.getHourlyVisits( 4, username);
        long five = aS.getHourlyVisits( 5, username);
        long six = aS.getHourlyVisits( 6, username);
        long seven = aS.getHourlyVisits( 7, username);
        long eight = aS.getHourlyVisits( 8, username);
        long nine = aS.getHourlyVisits( 9, username);
        long ten = aS.getHourlyVisits( 10, username);
        long eleven = aS.getHourlyVisits( 11, username);
        long twelve = aS.getHourlyVisits( 12, username);
        long thirteen = aS.getHourlyVisits( 13, username);
        long fourteen = aS.getHourlyVisits( 14, username);
        long fifteen = aS.getHourlyVisits( 15, username);
        long sixteen = aS.getHourlyVisits( 16, username);
        long seventeen = aS.getHourlyVisits( 17, username);
        long eightteen = aS.getHourlyVisits( 18, username);
        long nineteen = aS.getHourlyVisits( 19, username);
        long twenty = aS.getHourlyVisits( 20, username);
        long twenty_one = aS.getHourlyVisits( 21, username);
        long twenty_two = aS.getHourlyVisits( 22, username);
        long twenty_three = aS.getHourlyVisits( 23, username);

        attributes.put("zero", zero);
        attributes.put("one", one);
        attributes.put("two", two);
        attributes.put("three", three);
        attributes.put("four", four);
        attributes.put("five", five);
        attributes.put("six", six);
        attributes.put("seven", seven);
        attributes.put("eight", eight);
        attributes.put("nine", nine);
        attributes.put("ten", ten);
        attributes.put("eleven", eleven);
        attributes.put("twelve", twelve);
        attributes.put("thirteen", thirteen);
        attributes.put("fourteen", fourteen);
        attributes.put("fifteen", fifteen);
        attributes.put("sixteen", sixteen);
        attributes.put("seventeen", seventeen);
        attributes.put("eighteen", eightteen);
        attributes.put("nineteen", nineteen);
        attributes.put("twenty", twenty);
        attributes.put("twenty_one", twenty_one);
        attributes.put("twenty_two", twenty_two);
        attributes.put("twenty_three", twenty_three);
    }

    private static void addingDayStatsUser(Map<String, Object> attributes, String username) {
        AccesoService aS = AccesoService.getInstance();
        long cantMo = aS.getWeeklyVisits("MONDAY", username);
        long cantTue = aS.getWeeklyVisits("TUESDAY", username);
        long cantWen = aS.getWeeklyVisits("WEDNESDAY", username);
        long cantThu = aS.getWeeklyVisits("THURSDAY", username);
        long cantFri = aS.getWeeklyVisits("FRIDAY", username);
        long cantSat = aS.getWeeklyVisits("SATURDAY", username);
        long cantSun = aS.getWeeklyVisits("SUNDAY", username);

        attributes.put("mon", cantMo);
        attributes.put("tue", cantTue);
        attributes.put("wen", cantWen);
        attributes.put("thu", cantThu);
        attributes.put("fri", cantFri);
        attributes.put("sat", cantSat);
        attributes.put("sun", cantSun);
    }


    private static void addingBrowserStatsUser(Map<String, Object> attributes, String username) {
        AccesoService aS = AccesoService.getInstance();
        long cantChrome = aS.getBrowserVisits("Chrome", username);
        long cantSafari = aS.getBrowserVisits("Safari", username);
        long cantOpera = aS.getBrowserVisits("Opera", username);
        long cantEdge = aS.getBrowserVisits("Edge", username);
        long cantFirefox = aS.getBrowserVisits("Firefox", username);
        long cantBrave = aS.getBrowserVisits("Brave", username);
        attributes.put("cc", cantChrome);
        attributes.put("cs", cantSafari);
        attributes.put("co", cantOpera);
        attributes.put("ce", cantEdge);
        attributes.put("cf", cantFirefox);
        attributes.put("cb", cantBrave);
    }

    private static void addingGeneralSoStats(Map<String, Object> attributes) {
        AccesoService aS = AccesoService.getInstance();
        long android = aS.getGeneralOsVisits("Android");
        long iOs = aS.getGeneralOsVisits("iOS");
        long macOs = aS.getGeneralOsVisits("Mac OS");
        long linux = aS.getGeneralOsVisits("Linux");
        long ubuntu = aS.getGeneralOsVisits("Ubuntu");
        long windows = aS.getGeneralOsVisits("Windows");

        attributes.put("and", android);
        attributes.put("io", iOs);
        attributes.put("mo", macOs);
        attributes.put("li", linux);
        attributes.put("ub", ubuntu);
        attributes.put("wi", windows);
    }

    private static void addingGeneralHourStats(Map<String, Object> attributes) {
        AccesoService aS = AccesoService.getInstance();
        long zero = aS.getGeneralHourlyVisits(0);
        long one = aS.getGeneralHourlyVisits( 1);
        long two = aS.getGeneralHourlyVisits( 2);
        long three = aS.getGeneralHourlyVisits( 3);
        long four = aS.getGeneralHourlyVisits( 4);
        long five = aS.getGeneralHourlyVisits( 5);
        long six = aS.getGeneralHourlyVisits( 6);
        long seven = aS.getGeneralHourlyVisits( 7);
        long eight = aS.getGeneralHourlyVisits( 8);
        long nine = aS.getGeneralHourlyVisits( 9);
        long ten = aS.getGeneralHourlyVisits( 10);
        long eleven = aS.getGeneralHourlyVisits( 11);
        long twelve = aS.getGeneralHourlyVisits( 12);
        long thirteen = aS.getGeneralHourlyVisits( 13);
        long fourteen = aS.getGeneralHourlyVisits( 14);
        long fifteen = aS.getGeneralHourlyVisits( 15);
        long sixteen = aS.getGeneralHourlyVisits( 16);
        long seventeen = aS.getGeneralHourlyVisits( 17);
        long eightteen = aS.getGeneralHourlyVisits( 18);
        long nineteen = aS.getGeneralHourlyVisits( 19);
        long twenty = aS.getGeneralHourlyVisits( 20);
        long twenty_one = aS.getGeneralHourlyVisits( 21);
        long twenty_two = aS.getGeneralHourlyVisits( 22);
        long twenty_three = aS.getGeneralHourlyVisits( 23);

        attributes.put("zero", zero);
        attributes.put("one", one);
        attributes.put("two", two);
        attributes.put("three", three);
        attributes.put("four", four);
        attributes.put("five", five);
        attributes.put("six", six);
        attributes.put("seven", seven);
        attributes.put("eight", eight);
        attributes.put("nine", nine);
        attributes.put("ten", ten);
        attributes.put("eleven", eleven);
        attributes.put("twelve", twelve);
        attributes.put("thirteen", thirteen);
        attributes.put("fourteen", fourteen);
        attributes.put("fifteen", fifteen);
        attributes.put("sixteen", sixteen);
        attributes.put("seventeen", seventeen);
        attributes.put("eighteen", eightteen);
        attributes.put("nineteen", nineteen);
        attributes.put("twenty", twenty);
        attributes.put("twenty_one", twenty_one);
        attributes.put("twenty_two", twenty_two);
        attributes.put("twenty_three", twenty_three);
    }

    private static void addingGeneralDayStats(Map<String, Object> attributes) {
        AccesoService aS = AccesoService.getInstance();
        long cantMo = aS.getGeneralWeeklyVisits("MONDAY");
        long cantTue = aS.getGeneralWeeklyVisits("TUESDAY");
        long cantWen = aS.getGeneralWeeklyVisits("WEDNESDAY");
        long cantThu = aS.getGeneralWeeklyVisits("THURSDAY");
        long cantFri = aS.getGeneralWeeklyVisits("FRIDAY");
        long cantSat = aS.getGeneralWeeklyVisits("SATURDAY");
        long cantSun = aS.getGeneralWeeklyVisits("SUNDAY");

        attributes.put("mon", cantMo);
        attributes.put("tue", cantTue);
        attributes.put("wen", cantWen);
        attributes.put("thu", cantThu);
        attributes.put("fri", cantFri);
        attributes.put("sat", cantSat);
        attributes.put("sun", cantSun);
    }


    private static void addingGeneralBrowserStats(Map<String, Object> attributes) {
        AccesoService aS = AccesoService.getInstance();
        long cantChrome = aS.getGeneralBrowserVisits("Chrome");
        long cantSafari = aS.getGeneralBrowserVisits("Safari");
        long cantOpera = aS.getGeneralBrowserVisits("Opera");
        long cantEdge = aS.getGeneralBrowserVisits("Edge");
        long cantFirefox = aS.getGeneralBrowserVisits("Firefox");
        long cantBrave = aS.getGeneralBrowserVisits("Brave");
        attributes.put("cc", cantChrome);
        attributes.put("cs", cantSafari);
        attributes.put("co", cantOpera);
        attributes.put("ce", cantEdge);
        attributes.put("cf", cantFirefox);
        attributes.put("cb", cantBrave);
    }

    private static void createEntities(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Usuario adminUser = new Usuario(
                "admin",
                "admin",
                "admin",
                "admin",
                true
        );
        Usuario guessUser = new Usuario(
                "guess",
                "guess",
                "guess",
                "guess",
                false
        );
        URL urlInit = new URL(
                "/rd/prueba",
                "https://www.google.com.do/",
                new Timestamp(System.currentTimeMillis()),
                0,
                adminUser
        );
        UsuarioService.getInstance().crear(adminUser);
        UsuarioService.getInstance().crear(guessUser);
        URLService.getInstance().crear(urlInit);
    }



    private static void encriptingCookies(Request request, Map<String, Object> attributes) {
        attributes.put("editable", "no");
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(encriptorKey);
        Usuario usuario;
        if(request.cookie("username") != null){
            usuario = new Usuario(
                    textEncryptor.decrypt(request.cookie("username")),
                    textEncryptor.decrypt(request.cookie("nombre")),
                    textEncryptor.decrypt(request.cookie("apellido")),
                    textEncryptor.decrypt(request.cookie("password")),
                    Boolean.parseBoolean(textEncryptor.decrypt(request.cookie("isadmin")))
            );
            attributes.put("usuario", usuario);
        }else{
            attributes.put("usuario", "");
        }
    }

    public static void encriptarUsuario(List<Usuario> usuarios, Usuario usuario, String username, String password,
                                        Request request, Response response){
        for (Usuario usr : usuarios) {
            System.out.println(usr.toString());
            if (username.equalsIgnoreCase(usr.getUsername()) && password.equals(usr.getPassword())) {
                usuario = new Usuario(usr.getUsername(), usr.getNombre(), usr.getApellido(), usr.getPassword(), usr.isAdministrator());
                int recordar = (request.queryParams("recordar") != null ? 86400 : 1000);
                StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
                textEncryptor.setPassword(encriptorKey);
                String encriptedUsername = textEncryptor.encrypt(usuario.getUsername());
                String encriptedPassword = textEncryptor.encrypt(usuario.getPassword());
                String encriptedName = textEncryptor.encrypt(usuario.getNombre());
                String encriptedLastName = textEncryptor.encrypt(usuario.getApellido());
                String encriptedIsAdmin = textEncryptor.encrypt(String.valueOf(usuario.isAdministrator()));
                response.cookie("/", "username", encriptedUsername, recordar, false);
                response.cookie("/", "password", encriptedPassword, recordar, false);
                response.cookie("/", "nombre", encriptedName, recordar, false);
                response.cookie("/", "apellido", encriptedLastName, recordar, false);
                response.cookie("/", "isadmin", encriptedIsAdmin, recordar, false);
                response.redirect("/home");
            }
        }
    }
    /*TODO: Verificar si conviene más, por rendimiento, sacar esta función hacia un servicio.   */
    private static Usuario getCookieUser(Map<String, Object> attributes, Request request) {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword(encriptorKey);
        Usuario usuario = null;
        if(request.cookie("username") != null){
            usuario = new Usuario(
                    textEncryptor.decrypt(request.cookie("username")),
                    textEncryptor.decrypt(request.cookie("nombre")),
                    textEncryptor.decrypt(request.cookie("apellido")),
                    textEncryptor.decrypt(request.cookie("password")),
                    Boolean.parseBoolean(textEncryptor.decrypt(request.cookie("isadmin"))));
            usuario.setMisURLs(new HashSet<URL>());
            attributes.put("usuario", usuario);
        }else{
            usuario = UsuarioService.getInstance().find("guess");
            attributes.put("usuario", "");
        }
        return usuario;
    }

    static int getHerokuAssignedPort() { ProcessBuilder processBuilder = new ProcessBuilder(); if (processBuilder.environment().get("PORT") != null) { return Integer.parseInt(processBuilder.environment().get("PORT")); } return 4567; }
}


