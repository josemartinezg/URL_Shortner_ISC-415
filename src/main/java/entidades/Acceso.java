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
    @ManyToOne(optional = false)
    private URL urls;
    @OneToMany
    private Set<Navegador> misNavegadores;
    @OneToMany
    private Set<SistemaOperativo> misSOs;

    public Acceso(){}
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
}
