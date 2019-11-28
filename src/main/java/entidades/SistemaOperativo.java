package entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
@Entity
public class SistemaOperativo implements Serializable {
    @Id
    private int id;
    private String referencia_so;
    private String nomre_so;

    public SistemaOperativo(){}
    
    public SistemaOperativo(int id, String referencia_so, String nomre_so) {
        this.id = id;
        this.referencia_so = referencia_so;
        this.nomre_so = nomre_so;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferencia_so() {
        return referencia_so;
    }

    public void setReferencia_so(String referencia_so) {
        this.referencia_so = referencia_so;
    }

    public String getNomre_so() {
        return nomre_so;
    }

    public void setNomre_so(String nomre_so) {
        this.nomre_so = nomre_so;
    }

}
