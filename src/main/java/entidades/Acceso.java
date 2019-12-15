package entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Acceso implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String navegador;
    private String sistemaOperativo;
    private String ipCliente;
    private Date fechaHoraAcceso;
    private long hour;
    @Column(nullable = true)
    private String day;
    @ManyToOne(optional = false)
    private URL urls;
    @OneToMany
    private Set<Navegador> misNavegadores;
    @OneToMany
    private Set<SistemaOperativo> misSOs;

    public Acceso(){}
    public Acceso(String navegador, String sistemaOperativo, String ipCliente,
                  Date fechaHoraAcceso, URL urls) {
        this.navegador = navegador;
        this.sistemaOperativo = sistemaOperativo;
        this.ipCliente = ipCliente;
        this.fechaHoraAcceso = fechaHoraAcceso;
        this.urls = urls;
    }
    /*Todo: Constructor preferido.*/
    public Acceso(String navegador, String sistemaOperativo, String ipCliente,
                  Date fechaHoraAcceso, URL urls, long hour, String day) {
        this.navegador = navegador;
        this.sistemaOperativo = sistemaOperativo;
        this.ipCliente = ipCliente;
        this.fechaHoraAcceso = fechaHoraAcceso;
        this.urls = urls;
        this.hour = hour;
        this.day = day;
    }

    public Acceso(int id, String navegador, String sistemaOperativo, String ipCliente, Date fechaHoraAcceso) {
        this.id = id;
        this.navegador = navegador;
        this.sistemaOperativo = sistemaOperativo;
        this.ipCliente = ipCliente;
        this.fechaHoraAcceso = fechaHoraAcceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    public String getsistemaOperativo() {
        return sistemaOperativo;
    }

    public void setsistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public String getipCliente() {
        return ipCliente;
    }

    public void setipCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public Date getfechaHoraAcceso() {
        return fechaHoraAcceso;
    }

    public void setfechaHoraAcceso(Date fechaHoraAcceso) {
        this.fechaHoraAcceso = fechaHoraAcceso;
    }

    public Set<Navegador> getMisNavegadores() {
        return misNavegadores;
    }

    public void setMisNavegadores(Set<Navegador> misNavegadores) {
        this.misNavegadores = misNavegadores;
    }

    public Set<SistemaOperativo> getMisSOs() {
        return misSOs;
    }

    public void setMisSOs(Set<SistemaOperativo> misSOs) {
        this.misSOs = misSOs;
    }

    public URL getUrls() {
        return urls;
    }

    public void setUrls(URL urls) {
        this.urls = urls;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
