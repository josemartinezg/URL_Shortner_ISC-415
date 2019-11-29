package services;

import entidades.Acceso;

public class AccesoService extends BaseService<Acceso> {
    private static AccesoService accesoServiceInstance;

    public AccesoService(){
        super(Acceso.class);
    }

    public static AccesoService getInstance(){
        if (accesoServiceInstance == null){
            accesoServiceInstance = new AccesoService();
        }
        return accesoServiceInstance;
    }
}
