package services;
import entidades.URL;
import entidades.Usuario;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class UsuarioService  extends BaseService<Usuario>{
    private static UsuarioService usuarioServiceInstance;

    public UsuarioService(){
        super(Usuario.class);
    }

    public static UsuarioService getInstance(){
        if (usuarioServiceInstance == null){
            usuarioServiceInstance = new UsuarioService();
        }
        return usuarioServiceInstance;
    }

    public List<Usuario> selectAllWithoutAdminGuess(){
        String admin = "admin";
        String guess = "guess";
        Query query = getEntityManager().createQuery("Select u from Usuario u where u.username <>:admin and u.username <>:guess");
        query.setParameter("admin", admin);
        query.setParameter("guess", guess);
        try{
            return (List<Usuario>) query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }
}
