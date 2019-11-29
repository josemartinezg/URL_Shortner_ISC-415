package services;

import entidades.Navegador;
import entidades.Usuario;

public class NavegadorService extends BaseService<Navegador>{
    private static NavegadorService navegadorServiceInstance;

    public NavegadorService(){
        super(Navegador.class);
    }

    public static NavegadorService getInstance(){
        if (navegadorServiceInstance == null){
            navegadorServiceInstance = new NavegadorService();
        }
        return navegadorServiceInstance;
    }
}
