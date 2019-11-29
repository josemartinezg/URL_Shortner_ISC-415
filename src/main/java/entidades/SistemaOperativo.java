package entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class SistemaOperativo implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String referenciaSo;
    private String nombreSo;

    public SistemaOperativo(){}

    public SistemaOperativo(int id, String referenciaSo, String nombreSo) {
        this.id = id;
        this.referenciaSo = referenciaSo;
        this.nombreSo = nombreSo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getreferenciaSo() {
        return referenciaSo;
    }

    public void setreferenciaSo(String referenciaSo) {
        this.referenciaSo = referenciaSo;
    }

    public String getnombreSo() {
        return nombreSo;
    }

    public void setnombreSo(String nombreSo) {
        this.nombreSo = nombreSo;
    }

}
