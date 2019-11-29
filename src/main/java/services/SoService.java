package services;

import entidades.SistemaOperativo;

public class SoService extends BaseService<SistemaOperativo> {
    private static SoService soServiceInstance;

    public SoService(){
        super(SistemaOperativo.class);
    }

    public static SoService getInstance(){
        if (soServiceInstance == null){
            soServiceInstance = new SoService();
        }
        return soServiceInstance;
    }
}

