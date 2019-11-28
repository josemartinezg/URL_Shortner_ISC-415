package entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Acceso implements Serializable {
    @Id
    private int id;
    private String navegador;
    private String sistema_operativo;
    private String ip_cliente;
    private Date fecha_hora_acceso;
    private Set<Navegador> misNavegadores;
    private Set<SistemaOperativo> misSOs;

    public Acceso(){}
    public Acceso(int id, String navegador, String sistema_operativo, String ip_cliente, Date fecha_hora_acceso) {
        this.id = id;
        this.navegador = navegador;
        this.sistema_operativo = sistema_operativo;
        this.ip_cliente = ip_cliente;
        this.fecha_hora_acceso = fecha_hora_acceso;
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

    public String getSistema_operativo() {
        return sistema_operativo;
    }

    public void setSistema_operativo(String sistema_operativo) {
        this.sistema_operativo = sistema_operativo;
    }

    public String getIp_cliente() {
        return ip_cliente;
    }

    public void setIp_cliente(String ip_cliente) {
        this.ip_cliente = ip_cliente;
    }

    public Date getFecha_hora_acceso() {
        return fecha_hora_acceso;
    }

    public void setFecha_hora_acceso(Date fecha_hora_acceso) {
        this.fecha_hora_acceso = fecha_hora_acceso;
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
}
