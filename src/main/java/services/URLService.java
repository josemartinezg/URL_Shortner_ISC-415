package services;

import entidades.URL;

public class URLService extends BaseService<URL>{
    private static URLService URLServiceInstance;

    public URLService(){
        super(URL.class);
    }

    public static URLService getInstance(){
        if (URLServiceInstance == null){
            URLServiceInstance = new URLService();
        }
        return URLServiceInstance;
    }
}
