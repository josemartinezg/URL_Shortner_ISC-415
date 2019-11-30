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
import java.time.LocalDate;
import java.util.*;

import static spark.Spark.*;
public class Main {
    private static String encriptorKey = "aHaf920@_9";

    public static void main(String[] args){

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

        Spark.get("/", (request, response) -> {
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
                attributes.put("usuario", "");
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
            response.redirect("/login");

            return "";
        });
    Spark.post("/generarURL", (request, response) -> {
        Encoder encoder = new Encoder();
        String urlReferencia = request.queryParams("urlReferencia");
        Usuario guess = UsuarioService.getInstance().find("guess");
        URL url = new URL(urlReferencia);
        String urlGenerada = encoder.encode(urlReferencia);
        url.seturlGenerada(urlGenerada);
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
            url.setUsuario(usuario);
            usuario.getMisURLs().add(url);
            response.redirect("/vistaQrProvisional");
        }else{
            url.setUsuario(UsuarioService.getInstance().find("guess"));
            guess.getMisURLs().add(url);
            usuario = guess;
            response.redirect("/homeLink");
            //usuario.getMyAddresses().add(ipAddress);
        }
        /*Verificar mas tarde... Mientra tanto, él acorta.*/
        URLService.getInstance().crear(url);
        UsuarioService.getInstance().editar(usuario);
        response.redirect("/home");
        return "";
    });
    Spark.get("/vistaQrProvisional", (request, response) ->{
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
        URL urlAux = new URL("\rd\bg", "https://www.amazon.com/", 1, adminUser);
        ArrayList<URL> urlArrayList = new ArrayList<URL>();
        urlArrayList.add(urlAux);
        if(usuario != null){
            attributes.put("links", urlArrayList);
        }else{
            attributes.put("links", urlArrayList);
        }
        return new ModelAndView(attributes, "panelAdmin.ftl");
    }, freeMarkerEngine);

        Spark.get("/homeLink", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();

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
            Date fechaHora = new Date(System.currentTimeMillis());
            System.out.println(sistemaOperativo);
            Acceso acceso = new Acceso(navegador, sistemaOperativo, ipCliente, fechaHora, url);
            AccesoService.getInstance().crear(acceso);
            response.redirect("http://" + url.geturlReferencia());
        }else{
            response.redirect("/");
        }
        return "";
    });
        Spark.get("/generarReportes", (request, response) ->{
            Map<String, Object> attributes = new HashMap<>();
            Usuario usuario = request.session(true).attribute("usuario");
            attributes.put("usuario", usuario);
            if(usuario != null){
                attributes.put("links", UsuarioService.getInstance().find(usuario).
                        getMisURLs());
            }else{
                attributes.put("links", new ArrayList<>());
            }
            return new ModelAndView(attributes, "panelAdmin.ftl");
        }, freeMarkerEngine);

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
}


