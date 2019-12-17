//package main;
//import com.buck.common.codec.Codec;
//import com.google.gson.JsonSyntaxException;
//import entidades.ErrorResponse;
//import entidades.URL;
//import entidades.Usuario;
//import freemarker.template.Configuration;
//import services.*;
//import utils.JsonUtils;
//import spark.Request;
//import spark.Response;
//import spark.Spark;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import java.sql.Timestamp;
//
//import static spark.Spark.*;
////import static spark.debug.DebugScreen.enableDebugScreen;
//public class WebService {
//    public final static String ACCEPT_TYPE_JSON = "application/json";
//    public final static String ACCEPT_TYPE_XML = "application/xml";
//    public final static int BAD_REQUEST = 400;
//    public final static int ERROR_INTERNO = 500;
//    private static String contrasenia = "proyectoFinal";
//
//    public void manejarServicios() {
//        DataBaseService.getInstance().iniciarDb();
//        Configuration configuration = new Configuration(Configuration.getVersion());
//        configuration.setClassForTemplateLoading(Main.class, "/templates");
//        createEntities();
////        enableDebugScreen();
//        Codec codec = Codec.forName("Base16");
//       // Arranque.init();
//        exception(IllegalArgumentException.class, (exception, request, response) -> {
//            manejarError(BAD_REQUEST, exception, request, response);
//        });
//
//        exception(JsonSyntaxException.class, (exception, request, response) -> {
//            manejarError(BAD_REQUEST, exception, request, response);
//        });
//
//        exception(Exception.class, (exception, request, response) -> {
//            manejarError(ERROR_INTERNO, exception, request, response);
//        });
//        afterAfter("/*", (request, response) -> {
//            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
//            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
//            response.header("Access-Control-Allow-Origin", "*");
//            response.status(200);
//        });
//
//        path("/api", () -> {
//            Spark.get("/", (request, response) -> {
//                return "Empezando Acrotador URL";
//            }, JsonUtils.json());
////            Spark.post("/agregandoUrl", Main.ACCEPT_TYPE_JSON, (request, response) -> {
////                System.out.println(request.body());
////                UrlS url = new Gson().fromJson(request.body(), UrlS.class);
////                Shortener u = new Shortener(5, "www.tinyurl.com/");
////                url.setHashMaked(u.getKey(url.getUrl()));
////                UrlsService.getInstancia().crear(url);
////                // Shortener u = new Shortener(5, "www..com/");
////                return url;
////            }, JsonUtils.json());
//        });
//    }
//
//    private static void createEntities(){
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
//
//        Usuario adminUser = new Usuario(
//                "admin",
//                "admin",
//                "admin",
//                "admin",
//                true
//        );
//        Usuario guessUser = new Usuario(
//                "guess",
//                "guess",
//                "guess",
//                "guess",
//                false
//        );
//        URL urlInit = new URL(
//                "/rd/prueba",
//                "https://www.google.com.do/",
//                new Timestamp(System.currentTimeMillis()),
//                0,
//                adminUser
//        );
//        UsuarioService.getInstance().crear(adminUser);
//        UsuarioService.getInstance().crear(guessUser);
//        if(URLService.getInstance().selectUrlByUrlReferenciaAndUsuario(urlInit.geturlReferencia(), adminUser) == null){
//            URLService.getInstance().crear(urlInit);
//        }
//    }
//
//    private static void manejarError(int codigo, Exception exception, Request request, Response response) {
//        response.status(codigo);
//        response.body(JsonUtils.toJson(new ErrorResponse(100, exception.getMessage())));
//        exception.printStackTrace();
//    }
//}
