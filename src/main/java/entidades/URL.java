package entidades;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.criteria.Fetch;
@Entity
public class URL implements Serializable {
    @Id
    private long id;
    private String url_generada;
    private String url_referencia;
    private int cant_accesos;
    private Set<Acceso> misURLs;
    public URL(){}
    public URL(long id, String url_generada, String url_referencia, int cant_accesos) {
        this.id = id;
        this.url_generada = url_generada;
        this.url_referencia = url_referencia;
        this.cant_accesos = cant_accesos;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl_generada() {
        return url_generada;
    }

    public void setUrl_generada(String url_generada) {
        this.url_generada = url_generada;
    }

    public String getUrl_referencia() {
        return url_referencia;
    }

    public void setUrl_referencia(String url_referencia) {
        this.url_referencia = url_referencia;
    }

    public int getCant_accesos() {
        return cant_accesos;
    }

    public void setCant_accesos(int cant_accesos) {
        this.cant_accesos = cant_accesos;
    }

    public Set<Acceso> getMisURLs() {
        return misURLs;
    }

    public void setMisURLs(Set<Acceso> misURLs) {
        this.misURLs = misURLs;
    }

}
