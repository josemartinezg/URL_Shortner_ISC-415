package services;

import entidades.URL;

import javax.persistence.NoResultException;
import javax.persistence.Query;

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

    public URL selectUrlGenerada(String urlGenerada){
        Query query = getEntityManager().createQuery("SELECT ur FROM URL ur WHERE ur.urlGenerada =: urlGenerada");
        query.setParameter("urlGenerada", urlGenerada);
        try{
            return (URL) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
