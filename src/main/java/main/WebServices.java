package main;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entidades.Acceso;
import entidades.ErrorResponse;
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
import utils.JsonUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static spark.Spark.exception;
import static spark.Spark.path;

public class WebServices {
    public final static String ACCEPT_TYPE_JSON = "application/json";
    public final static String ACCEPT_TYPE_XML = "application/xml";
    public final static int BAD_REQUEST = 400;
    public final static int ERROR_INTERNO = 500;

    public void manejarServicios(){
        System.out.println("Proyecto Demo SparkJava RESTFUL");

//        //Clase que representa el servicio.
//        EstudianteService estudianteService = EstudianteService.getInstancia();

        //Manejo de Excepciones.
        exception(IllegalArgumentException.class, (exception, request, response) -> {
            manejarError(BAD_REQUEST, exception, request, response);
        });

        exception(JsonSyntaxException.class, (exception, request, response) -> {
            manejarError(BAD_REQUEST, exception, request, response);
        });

        exception(Exception.class, (exception, request, response) -> {
            manejarError(ERROR_INTERNO, exception, request, response);
        });
        path("/api", () ->{
            Spark.get("/api", (request, response) -> {
                return "<h1> REST API CHINDE.LINK PARA ACORTAR URLS </h1>";
            });
            Spark.get("/urls/:usuario", (request, response) -> {
                ArrayList<URL> arrayListUrls = new ArrayList<>();
                Usuario usuario = UsuarioService.getInstance().find(request.params("usuario"));
                if (usuario.getMisURLs().size() <= 0) {
                    return null;
                }
                for (URL u: usuario.getMisURLs()) {
                    u.setUsuario(null);
                    u.setMisURLs(null);
                    arrayListUrls.add(u);
                }
                return arrayListUrls;
            }, JsonUtils.json());
            Spark.get("/urls/:usuario/:idUrl", (request, response) -> {
                ArrayList<Acceso> arrayListAccesos = new ArrayList<>();
                Usuario usuario = UsuarioService.getInstance().find(request.params("usuario"));
                URL url = URLService.getInstance().find(Long.parseLong(request.params("idUrl")));
                if (url.getMisURLs().size() <= 0) {
                    return null;
                }
                for (Acceso acc: url.getMisURLs()) {
                    acc.setUrls(null);
                    acc.setMisNavegadores(null);
                    acc.setMisSOs(null);
                    arrayListAccesos.add(acc);
                }
                return arrayListAccesos;
            }, JsonUtils.json());
            Spark.post("/generarURL/:usuario", ACCEPT_TYPE_JSON, (request, response) -> {
                Usuario usuario = UsuarioService.getInstance().find(request.params("usuario"));
                URL url = new Gson().fromJson(request.body(), URL.class);
                url.setUsuario(usuario);
                String enc = new Encoder().encode(url.geturlReferencia());
                url.seturlGenerada(enc);
                System.out.println(enc);
                URLService.getInstance().generarURL(url.geturlReferencia(), url.getUsuario());
                URLService.getInstance().crear(url);
                url.setMisURLs(null);
                url.setUsuario(null);
                return url;
            }, JsonUtils.json());
        });
    }

    private static void manejarError(int codigo, Exception exception, Request request, Response response) {
        response.status(codigo);
        response.body(JsonUtils.toJson(new ErrorResponse(100, exception.getMessage())));
        exception.printStackTrace();
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        } return 4567;
    }
}

