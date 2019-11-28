package entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class Navegador implements Serializable {
    @Id
    private int id;
    private String referencia_navegador;
    private String nombre_navegador;

    public Navegador(){}

    public Navegador(int id, String referencia_navegador, String nombre_navegador) {
        this.id = id;
        this.referencia_navegador = referencia_navegador;
        this.nombre_navegador = nombre_navegador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferencia_navegador() {
        return referencia_navegador;
    }

    public void setReferencia_navegador(String referencia_navegador) {
        this.referencia_navegador = referencia_navegador;
    }

    public String getNombre_navegador() {
        return nombre_navegador;
    }

    public void setNombre_navegador(String nombre_navegador) {
        this.nombre_navegador = nombre_navegador;
    }
}
