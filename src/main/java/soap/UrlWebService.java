package soap;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entidades.Acceso;
import entidades.URL;
import entidades.Usuario;
import services.AccesoService;
import services.URLService;
import services.UsuarioService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

@WebService
public class UrlWebService {
    @WebMethod
    public String generarURL(String urlReferencia, String username){
        Usuario guess = UsuarioService.getInstance().find(username);
        URL url = URLService.getInstance().generarUrlWithReturn(urlReferencia, guess);
        String vistaPrevia= "https://api.linkpreview.net/?key=5df79533328289b6e0fb52eba76006d3d11ead82fad4d&q=" + urlReferencia;
        HttpResponse<JsonNode> APIResultado = Unirest.get(vistaPrevia).asJson();
        //Creating un string para guardar la data de la imagen
        String image = APIResultado.getBody().getObject().getString("image");
        //Creating un string con nombre de base64 para diferenciarlo pq este ya va a estar encoded
        String imagenBase64 = Base64.getEncoder().encodeToString(image.getBytes());
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("urlReferencia", urlReferencia);
        jsonObject.addProperty("urlGenerado", url.geturlGenerada());
        jsonObject.addProperty("fechaCreacion", url.getFechaCreacion().toString());
        jsonObject.addProperty("UsuarioCreador", url.getUsuario().getUsername());
        System.out.println(imagenBase64);
        jsonObject.addProperty("imagenBase64", imagenBase64);
        String json = gson.toJson(jsonObject);
        System.out.println(json);
        return json;
    }

    @WebMethod
    public ArrayList<String> getUrls(String username){
        Usuario usuario = UsuarioService.getInstance().find(username);
        ArrayList<URL> arrayListUrls = new ArrayList<>();
        Gson gson = new Gson();
        if (usuario.getMisURLs().size() <= 0) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        ArrayList<String> jsonUrls = new ArrayList<>();
        for (URL u: usuario.getMisURLs()) {
            //u.setUsuario(null);
            //u.setMisURLs(null);
            arrayListUrls.add(u);

            String vistaPrevia= "https://api.linkpreview.net/?key=5df79533328289b6e0fb52eba76006d3d11ead82fad4d&q=" + u.geturlReferencia();
            HttpResponse<JsonNode> APIResultado = Unirest.get(vistaPrevia).asJson();
            //Creating un string para guardar la data de la imagen
            String image = APIResultado.getBody().getObject().getString("image");
            //Creating un string con nombre de base64 para diferenciarlo pq este ya va a estar encoded
            String imagenBase64 = Base64.getEncoder().encodeToString(image.getBytes());
            jsonObject.addProperty("urlReferencia", u.geturlReferencia());
            jsonObject.addProperty("urlGenerado", u.geturlGenerada());
            jsonObject.addProperty("fechaCreacion", u.getFechaCreacion().toString());
            jsonObject.addProperty("usuarioCreador", u.getUsuario().getUsername());
            System.out.println(imagenBase64);
            jsonObject.addProperty("imagenBase64", imagenBase64);
            JsonArray accesos = new JsonArray();
            JsonObject jsonAcceso = new JsonObject();
            for (Acceso acceso: u.getMisURLs()) {
                jsonAcceso.addProperty("urlReferencia", acceso.getUrls().geturlReferencia());
                jsonAcceso.addProperty("sistemaOperativo", acceso.getsistemaOperativo());
                jsonAcceso.addProperty("navegador", acceso.getNavegador());
                jsonAcceso.addProperty("ipCliente", acceso.getipCliente());
                jsonAcceso.addProperty("fechaAcceso", acceso.getfechaHoraAcceso().toString());
                accesos.add(jsonAcceso);
            }
            jsonObject.addProperty("accesos", gson.toJson(accesos));
            String json = gson.toJson(jsonObject);
            jsonUrls.add(json);
        }

        return jsonUrls;
    }

    @WebMethod
    public List<Acceso> getUrlsByAccesos(String username){
        Usuario usuario = UsuarioService.getInstance().find(username);
        ArrayList<URL> arrayListUrls = new ArrayList<>();
        List<Acceso> accesos = AccesoService.getInstance().getAccesosByUsername(username);
        System.out.println(accesos.toString());
        return accesos;
    }
}
