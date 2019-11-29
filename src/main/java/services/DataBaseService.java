package services;

import entidades.Usuario;
import org.h2.tools.Server;
import java.sql.SQLException;

public class DataBaseService {
    private static DataBaseService instance;

    public static DataBaseService getInstance() {
        if (instance == null) {
            instance = new DataBaseService();
        }
        return instance;
    }

    public void iniciarDb() {
//        Usuario adminUser = new Usuario("admin", "admin", "admin",
//                "admin", true);
//        if (UsuarioService.getInstance().find(adminUser) == null){
//            UsuarioService.getInstance().crear(adminUser);
//        }
        try {
            Server.createTcpServer("-tcpPort","9092","-tcpAllowOthers","-tcpDaemon").start();

        } catch (SQLException ex) {
            System.out.println("Database error: "+ex.getMessage());
        }
    }
    public void init(){
        iniciarDb();
    }
}
