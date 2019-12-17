package services;

import entidades.URL;
import entidades.Usuario;
import utils.Encoder;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

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
        Query query = getEntityManager().createQuery("SELECT ur FROM URL ur WHERE ur.urlGenerada =:urlGenerada");
        query.setParameter("urlGenerada", urlGenerada);
        try{
            return (URL) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public URL selectUrlByUrlReferencia(String urlReferencia){
        Query query = getEntityManager().createQuery("SELECT ur FROM URL ur WHERE ur.urlReferencia =:urlReferencia");
        query.setParameter("urlReferencia", urlReferencia);
        try{
            return (URL) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public URL selectUrlByUrlReferenciaAndUsuario(String urlReferencia, Usuario usuario){
        Query query = getEntityManager().createQuery("SELECT ur FROM URL ur WHERE ur.urlReferencia =:urlReferencia and ur.usuario =:usuario");
        query.setParameter("urlReferencia", urlReferencia);
        query.setParameter("usuario", usuario);

        try{
            return (URL) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }


    public List<URL> selectUrlByUsername(String username){
        Query query = getEntityManager().createQuery("Select u from URL u where u.usuario.username =:username");
        query.setParameter("username", username);
        try{
            return (List<URL>) query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public void generarURL(String urlReferencia, Usuario usuario) {
        URL url = URLService.getInstance().selectUrlByUrlReferenciaAndUsuario(urlReferencia, usuario);
        if(url == null) {
            url = new URL(urlReferencia);
        }
        Encoder encoder = new Encoder();
        String urlGenerada = encoder.encode(urlReferencia);
        url.seturlGenerada(urlGenerada);
        url.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        usuario.setMisURLs(new HashSet<URL>());
        url.setUsuario(usuario);
        usuario.getMisURLs().add(url);
        URLService.getInstance().crear(url);
        UsuarioService.getInstance().editar(usuario);
    }

    public URL generarUrlWithReturn(String urlReferencia, Usuario usuario) {
        URL url = URLService.getInstance().selectUrlByUrlReferenciaAndUsuario(urlReferencia, usuario);
        if(url == null) {
            url = new URL(urlReferencia);
        }
        Encoder encoder = new Encoder();
        String urlGenerada = encoder.encode(urlReferencia);
        url.seturlGenerada(urlGenerada);
        url.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        usuario.setMisURLs(new HashSet<URL>());
        url.setUsuario(usuario);
        usuario.getMisURLs().add(url);
        URLService.getInstance().crear(url);
        UsuarioService.getInstance().editar(usuario);
        return url;
    }

    public long getSizeById() {
        Query query = getEntityManager()
                .createNativeQuery("SELECT ID FROM URL \n" +
                        "ORDER BY ID \n" +
                        "DESC \n" +
                        "LIMIT 1");
        return ((BigInteger) query.getSingleResult()).longValue();
    }
}
