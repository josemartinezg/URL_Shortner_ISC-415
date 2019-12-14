package services;

import entidades.Acceso;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.Query;

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

}
