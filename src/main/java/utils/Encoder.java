package utils;

import entidades.URL;
import services.URLService;

import java.util.HashMap;
import java.util.Map;

public class Encoder {
    private static final String BASE62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private Long counter;
    private Map<Long, String> indexToUrl;
    private Map<String, Long> urlToIndex;

    public Encoder(){
        counter = 1L;
        indexToUrl = new HashMap<>();
        urlToIndex = new HashMap<>();
    }

    public String encode(String urlReferencia){
        System.out.println("URL Original: " + urlReferencia);
        long urlCurrentIndex = URLService.getInstance().getSizeById()+1;
        String currentIndexedUrl = "/chinde.link/" + Long.toString(urlCurrentIndex);
        if (urlToIndex.containsKey(urlReferencia)){
            System.out.println("/rd/"+base62Encode(urlToIndex.get(urlReferencia)));
            return "/rd/" + base62Encode(urlToIndex.get(urlReferencia));
        }else{
            indexToUrl.put(urlCurrentIndex, urlReferencia);
            urlToIndex.put(urlReferencia, urlCurrentIndex);
            URL url = new URL(urlReferencia);
            System.out.println("El valor de urlReferencia en este punto es: " + currentIndexedUrl);
           // url.seturlGenerada("/chinde.link/"+base62Encode(urlToIndex.get(urlReferencia)));
//            URLService.getInstance().crear(url);
            //Controladora.getInstance().getMisUrls().add(url);
            String url2 = "/rd/"+base62Encode(urlToIndex.get(urlReferencia));
            System.out.println(urlToIndex.get(urlReferencia));
            System.out.println("/rd/"+base62Encode(urlToIndex.get(urlReferencia)));
            return url2;
        }
    }
    private String base62Encode(long value){
        StringBuilder sb = new StringBuilder();
        while (value != 0) {
            sb.append(BASE62.charAt((int)(value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }
}
