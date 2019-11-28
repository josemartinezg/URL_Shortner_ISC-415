package entidades;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.criteria.Fetch;
@Entity
public class Usuario implements Serializable{
    @Id
    private String username;
    private String nombre;
    private String apellido;
    private String password;
    private boolean administrator;
    private Set<URL> misURLs;
}
