package entidades;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.criteria.Fetch;
@Entity
public class URL implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String urlGenerada;
    private String urlReferencia;
    private int cantAccesos;
    @ManyToOne(optional = false)
    private Usuario usuarios;
    @OneToMany(mappedBy = "urls", fetch = FetchType.LAZY)
    private Set<Acceso> misURLs;

    public URL(){}
    public URL(long id, String urlGenerada, String urlReferencia, int cantAccesos) {
        this.id = id;
        this.urlGenerada = urlGenerada;
        this.urlReferencia = urlReferencia;
        this.cantAccesos = cantAccesos;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String geturlGenerada() {
        return urlGenerada;
    }

    public void seturlGenerada(String urlGenerada) {
        this.urlGenerada = urlGenerada;
    }

    public String geturlReferencia() {
        return urlReferencia;
    }

    public void seturlReferencia(String urlReferencia) {
        this.urlReferencia = urlReferencia;
    }

    public int getcantAccesos() {
        return cantAccesos;
    }

    public void setcantAccesos(int cantAccesos) {
        this.cantAccesos = cantAccesos;
    }

    public Set<Acceso> getMisURLs() {
        return misURLs;
    }

    public void setMisURLs(Set<Acceso> misURLs) {
        this.misURLs = misURLs;
    }

    public Usuario getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuarios = usuarios;
    }
}
