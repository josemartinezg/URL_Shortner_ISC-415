package main;
import entidades.Usuario;
import freemarker.template.Configuration;
import org.jasypt.util.text.StrongTextEncryptor;
import services.DataBaseService;
import services.UsuarioService;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Spark.get("/", (request, response) -> {
            response.redirect("/home");
            return "";
        });

        Spark.get("/home", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Home");
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
            encriptarUsuario(usuarios, usuario, username, password, request, response);
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
        UsuarioService.getInstance().crear(adminUser);
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


