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
    private Usuario usuario;
    @OneToMany(mappedBy = "urls", fetch = FetchType.EAGER)
    private Set<Acceso> misURLs;

    public URL(){}
    public URL(String urlReferencia){
        this.urlReferencia = urlReferencia;
    }
    public URL(String urlGenerada, String urlReferencia, int cantAccesos, Usuario usuario) {
        this.urlGenerada = urlGenerada;
        this.urlReferencia = urlReferencia;
        this.cantAccesos = cantAccesos;
        this.usuario = usuario;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
