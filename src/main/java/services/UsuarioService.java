package services;
import entidades.Usuario;

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
}
