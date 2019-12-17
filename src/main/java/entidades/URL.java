package entidades;

import services.AccesoService;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.criteria.Fetch;
@Entity
public class URL implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Column(length = 1024)
    private String urlGenerada;
    @Column(length = 1024)
    private String urlReferencia;
    private Timestamp fechaCreacion;
    private int cantAccesos;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Usuario usuario;
    @OneToMany(mappedBy = "urls", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Acceso> misURLs;

    public URL(){
        this.cantAccesos = 0;
    }
    public URL(String urlReferencia){
        this.urlReferencia = urlReferencia;
        this.cantAccesos = 0;
    }

    public URL(String urlGenerada, String urlReferencia, Timestamp fechaCreacion, int cantAccesos, Usuario usuario) {
        this.urlGenerada = urlGenerada;
        this.urlReferencia = urlReferencia;
        this.fechaCreacion = fechaCreacion;
        this.cantAccesos = cantAccesos;
        this.usuario = usuario;
    }

    public URL(String urlGenerada, String urlReferencia, Usuario usuario) {
        this.id = id;
        this.urlGenerada = urlGenerada;
        this.urlReferencia = urlReferencia;
        this.cantAccesos = 0;
        this.usuario = usuario;
    }
    public URL(long id, String urlGenerada, String urlReferencia) {
        this.id = id;
        this.urlGenerada = urlGenerada;
        this.urlReferencia = urlReferencia;
        this.cantAccesos = 0;
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

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getTopAccesoFechaMasReciente(){
        Acceso acceso = AccesoService.getInstance().getTopAccesoByUrlDateTime(this.id);
        if(acceso == null){
            acceso = new Acceso("", "", "", new Date(2000, Calendar.JANUARY, 1, 1, 1, 1), new URL(), 0, "");
        }
        return new Timestamp(acceso.getfechaHoraAcceso().getTime());
    }

    @Override
    public String toString() {
        return "URL{" +
                "id=" + id +
                ", urlGenerada='" + urlGenerada + '\'' +
                ", urlReferencia='" + urlReferencia + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
