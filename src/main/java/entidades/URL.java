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
}
