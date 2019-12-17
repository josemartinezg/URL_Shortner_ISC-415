package services;

import entidades.Acceso;
import entidades.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

public class AccesoService extends BaseService<Acceso> {
    private static AccesoService accesoServiceInstance;

    public AccesoService(){
        super(Acceso.class);
    }

    public static AccesoService getInstance(){
        if (accesoServiceInstance == null){
            accesoServiceInstance = new AccesoService();
        }
        return accesoServiceInstance;
    }

    public List<Acceso> getAccesosByUrl(long urlConsulta){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("from Acceso where urls.id=:urlConsulta");
        query.setParameter("urlConsulta", urlConsulta);
        return query.getResultList();
    }

    public List<Acceso> getAccesosByUsername(String username){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("from Acceso where urls.usuario.username=:username");
        query.setParameter("username", username);
        return (List<Acceso>) query.getResultList();
    }

    public Acceso getTopAccesoByUrlDateTime(long urlConsulta){
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("from Acceso where urls.id=:urlConsulta ORDER BY fechaHoraAcceso DESC");
        query.setParameter("urlConsulta", urlConsulta);
        query.setMaxResults(1);
        Acceso acceso;
        try{
            acceso = (Acceso) query.getSingleResult();
        }catch(NoResultException e){
            acceso = null;
        }
        return acceso;
    }

    public long getCantAccesosByUrl(String urlConsulta){
        Query query = getEntityManager().createQuery("Select count(acc.id) from Acceso acc " +
                "where acc.urls.urlGenerada=:urlConsulta");
        query.setParameter("urlConsulta", urlConsulta);
        return (long) query.getSingleResult();
    }

    public void updateAccessQuantity(long cantVisitas, long urlId){

        /*TODO: Transation required for later.*/
//        SessionFactory factory = HibernateUtility.getSessionFactory();
//
//        Session session = factory.openSession();
//        Transaction transaction = session.beginTransaction();
//        Query query = getEntityManager()
//                .createQuery("update URL set cantAccesos=:cantVisitas where id=:urlId");
//        query.setParameter("cantVisitas", (int)cantVisitas);
//        query.setParameter("urlId", urlId);
//        int result  = query.executeUpdate();
//        System.out.println(result);
    }

    public long getCantAccesosByBrowser(String browser, long urlId){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.id=:urlId " +
                        "and acc.navegador=:browser");
        query.setParameter("urlId", urlId);
        query.setParameter("browser", browser);
        return (long) query.getSingleResult();
    }

    public long getCantAccesosByHour(long hour, long urlId){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.id=:urlId " +
                        "and acc.hour=:hour");
        query.setParameter("urlId", urlId);
        query.setParameter("hour", hour);
        return (long) query.getSingleResult();
    }

    public long getCantAccesosByDayOfWeek(String day, long urlId){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.id=:urlId " +
                        "and acc.day=:day");
        query.setParameter("urlId", urlId);
        query.setParameter("day", day);
        return (long) query.getSingleResult();
    }
    public long getCantAccesosOs(String so, long urlId){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.id=:urlId " +
                        "and acc.sistemaOperativo=:so");
        query.setParameter("urlId", urlId);
        query.setParameter("so", so);
        return (long) query.getSingleResult();
    }

    public long getBrowserVisits(String browser, String username){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.usuario.username=:username " +
                        "and acc.navegador=:browser");
        query.setParameter("username", username);
        query.setParameter("browser", browser);
        return (long) query.getSingleResult();
    }

    public long getHourlyVisits(long hour, String username){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.usuario.username=:username " +
                        "and acc.hour=:hour");
        query.setParameter("username", username);
        query.setParameter("hour", hour);
        return (long) query.getSingleResult();
    }

    public long getWeeklyVisits(String day, String username){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.usuario.username=:username " +
                        "and acc.day=:day");
        query.setParameter("username", username);
        query.setParameter("day", day);
        return (long) query.getSingleResult();
    }
    public long getOsVisits(String so, String username){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.urls.usuario.username=:username " +
                        "and acc.sistemaOperativo=:so");
        query.setParameter("username", username);
        query.setParameter("so", so);
        return (long) query.getSingleResult();
    }
    public long getGeneralBrowserVisits(String browser){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.navegador=:browser");
        query.setParameter("browser", browser);
        return (long) query.getSingleResult();
    }

    public long getGeneralHourlyVisits(long hour){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.hour=:hour");
        query.setParameter("hour", hour);
        return (long) query.getSingleResult();
    }

    public long getGeneralWeeklyVisits(String day){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.day=:day");
        query.setParameter("day", day);
        return (long) query.getSingleResult();
    }
    public long getGeneralOsVisits(String so){
        Query query = getEntityManager()
                .createQuery("Select count (acc.id) " +
                        "from Acceso acc " +
                        "where acc.sistemaOperativo=:so");
        query.setParameter("so", so);
        return (long) query.getSingleResult();
    }

}
