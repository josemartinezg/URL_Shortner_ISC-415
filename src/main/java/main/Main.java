package main;
import entidades.Usuario;
import freemarker.template.Configuration;
import services.DataBaseService;
import services.UsuarioService;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
public class Main {
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

}

