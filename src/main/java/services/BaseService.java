package services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class BaseService<T> {
    private static EntityManagerFactory emFactory;
    private Class<T> claseEntidad;

    public BaseService(Class<T> claseEntidad){
        if (emFactory == null){
            /*Bootstrap class that is used to obtain an EntityManagerFactory in Java SE environments. It may also be
            used to cause schema generation to occur.*/
            emFactory = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        }
        this.claseEntidad = claseEntidad;
    }
    public EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }

    /**
     * Metodo para obtener el valor del campo anotado como @ID.
     * @param entidad
     * @return
     */
    private Object getValorCampo(T entidad){
        if(entidad == null){
            return null;
        }
        //aplicando la clase de reflexión.
        for(Field f : entidad.getClass().getDeclaredFields()) {  //tomando todos los campos privados.
            if (f.isAnnotationPresent(Id.class)) {
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entidad);
                    return valorCampo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     *
     * @param entidad
     */
    public void crear(T entidad){
        EntityManager entityManager = getEntityManager();
        System.out.println("========!!!=============");
        System.out.println(entidad.toString());
        try {
            if (entityManager.find(claseEntidad, getValorCampo(entidad)) != null) {
                System.out.println("La entidad a guardar existe, no creada.");
                return;
            }
        }catch (IllegalArgumentException ie){
            //
            System.out.println("Parametro ilegal.");
        }

        entityManager.getTransaction().begin();
        try {
            entityManager.persist(entidad);
            entityManager.getTransaction().commit();

        }catch (Exception ex){
            entityManager.getTransaction().rollback();
            throw  ex;
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @param entidad
     */
    public void editar(T entidad){
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(entidad);
            entityManager.getTransaction().commit();
        }catch (Exception ex){
            entityManager.getTransaction().rollback();
            throw  ex;
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @param entidadId
     */
    public void eliminar(Object  entidadId){
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        try {
            T entidad = entityManager.find(claseEntidad, entidadId);
            entityManager.remove(entidad);
            entityManager.getTransaction().commit();
        }catch (Exception ex){
            entityManager.getTransaction().rollback();
            throw  ex;
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public T find(Object id) {
        EntityManager entityManager = getEntityManager();
        try{
            return entityManager.find(claseEntidad, id);
        } catch (Exception ex){
            throw  ex;
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @return
     */
    public List<T> findAll(){
        EntityManager entityManager = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex){
            throw  ex;
        }finally {
            entityManager.close();
        }
    }
/*Optional from Practica 4*/
    public T findByColumn(List<String> column, List<String> value){
        EntityManager entityManager = getEntityManager();
        try{
            /*riteriaBuilder permite crear queries con objetos de Java, haciéndolos más dinámicos, ya que se crean en
            tiempo de corida aquí le pasamos la entidad de cualquier objeto que sea extendido desde esta clase.*/
            CriteriaQuery<T> criteriaQuery = entityManager
                    .getCriteriaBuilder().createQuery(claseEntidad);
            Root<T> obtained = criteriaQuery.from(claseEntidad);
            criteriaQuery.select(obtained);
            for (int idx = 0; idx < column.size(); idx++){
                criteriaQuery.where((entityManager.getCriteriaBuilder().equal(obtained.get(column.get(idx)), value.get(idx))));
            }
            List<T> result = entityManager.createQuery(criteriaQuery).getResultList();
            if (result.size() > 0){
                return result.get(0);
            }else{
                return null;
            }
        } catch (Exception ex){
            entityManager.close();
            throw ex;
        } finally {
            entityManager.close();
        }
    }

}
