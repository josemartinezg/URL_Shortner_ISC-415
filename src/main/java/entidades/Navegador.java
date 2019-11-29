package entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class Navegador implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String referenciaNavegador;
    private String nombreNavegador;

    public Navegador(){}

    public Navegador(int id, String referenciaNavegador, String nombreNavegador) {
        this.id = id;
        this.referenciaNavegador = referenciaNavegador;
        this.nombreNavegador = nombreNavegador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getreferenciaNavegador() {
        return referenciaNavegador;
    }

    public void setreferenciaNavegador(String referenciaNavegador) {
        this.referenciaNavegador = referenciaNavegador;
    }

    public String getnombreNavegador() {
        return nombreNavegador;
    }

    public void setnombreNavegador(String nombreNavegador) {
        this.nombreNavegador = nombreNavegador;
    }

}
