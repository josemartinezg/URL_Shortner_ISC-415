package entidades;

import jdk.internal.jline.internal.Urls;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
@Entity
public class Usuario implements Serializable{
    @Id
    private String username;
    private String nombre;
    private String apellido;
    private String password;
    private boolean administrator;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private Set<URL> misURLs;
//    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
//    private  Set<IpAddress> myAddresses;

    public  Usuario(){}
    public Usuario(String username, String nombre, String apellido,
                   String password, boolean administrator) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.administrator = administrator;
    }
    public Usuario(String username, String nombre, String apellido,
                   String password, boolean administrator, Set<URL> misUrls) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.administrator = administrator;
        this.misURLs = misUrls;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public Set<URL> getMisURLs() {
        return misURLs;
    }

    public void setMisURLs(Set<URL> misURLs) {
        this.misURLs = misURLs;
    }

//    public Set<IpAddress> getMyAddresses() {
//        return myAddresses;
//    }
//
//    public void setMyAddresses(Set<IpAddress> myAddresses) {
//        this.myAddresses = myAddresses;
//    }

}
