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
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(encriptorKey);
            Usuario usuario;
            if(request.cookie("username") != null){
                usuario = new Usuario(
                        textEncryptor.decrypt(request.cookie("username")),
                        textEncryptor.decrypt(request.cookie("nombre")),
                        textEncryptor.decrypt(request.cookie("apellido")),
                        textEncryptor.decrypt(request.cookie("password")),
                        Boolean.parseBoolean(textEncryptor.decrypt(request.cookie("isadmin"))));
                attributes.put("usuario", usuario);
            }else{
                usuario = UsuarioService.getInstance().find("guess");
                attributes.put("usuario", "");
            }
            String urlReferencia = request.cookie("url_referencia");
            if(urlReferencia == null){
                attributes.put("urlreferencia", "");
            }else{
                String urlGenerado = URLService.getInstance().selectUrlByUrlReferenciaAndUsuario(urlReferencia, usuario).geturlGenerada();
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
            return new ModelAndView(attributes, "login.ftl");
        }, freeMarkerEngine);

        Spark.get("/register", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Register");
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
                attributes.put("usuario", "");
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
            long urlId = Long.valueOf(request.params("idCampaign"));
            String username = request.session(true).attribute("usuario");
            Usuario usuario = UsuarioService.getInstance().find(username);
            attributes.put("usuario", usuario);
            addingBrowserStats(attributes, urlId);
            addingDayStats(attributes, urlId);
            addingHourStats(attributes, urlId);
//            if(usuario != null){
//                attributes.put("links", UsuarioService.getInstance().find(usuario).
//                        getMisURLs());
//            }else{
//                attributes.put("links", new ArrayList<>());
//            }
            return new ModelAndView(attributes, "campaign-statistics.ftl");
        }, freeMarkerEngine);

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
        UsuarioService.getInstance().crear(adminUser);
        UsuarioService.getInstance().crear(guessUser);
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
    static int getHerokuAssignedPort() { ProcessBuilder processBuilder = new ProcessBuilder(); if (processBuilder.environment().get("PORT") != null) { return Integer.parseInt(processBuilder.environment().get("PORT")); } return 4567; }
}


