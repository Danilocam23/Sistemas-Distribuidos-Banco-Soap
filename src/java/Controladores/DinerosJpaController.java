/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Cuentas;
import Entidades.Dineros;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author xcojcama
 */
public class DinerosJpaController implements Serializable {

    public DinerosJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Banco_SocketPU");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dineros dineros) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuentas IDCuentas = dineros.getIDCuentas();
            if (IDCuentas != null) {
                IDCuentas = em.getReference(IDCuentas.getClass(), IDCuentas.getIDCuentas());
                dineros.setIDCuentas(IDCuentas);
            }
            em.persist(dineros);
            if (IDCuentas != null) {
                IDCuentas.getDinerosList().add(dineros);
                IDCuentas = em.merge(IDCuentas);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

     public void edit(Dineros dineros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dineros persistentDineros = em.find(Dineros.class, dineros.getIDDinero());
            Cuentas IDCuentasOld = persistentDineros.getIDCuentas();
            Cuentas IDCuentasNew = dineros.getIDCuentas();
            if (IDCuentasNew != null) {
                IDCuentasNew = em.getReference(IDCuentasNew.getClass(), IDCuentasNew.getIDCuentas());
                dineros.setIDCuentas(IDCuentasNew);
            }
            dineros = em.merge(dineros);
            if (IDCuentasOld != null && !IDCuentasOld.equals(IDCuentasNew)) {
                IDCuentasOld.getDinerosList().remove(dineros);
                IDCuentasOld = em.merge(IDCuentasOld);
            }
            if (IDCuentasNew != null && !IDCuentasNew.equals(IDCuentasOld)) {
                IDCuentasNew.getDinerosList().add(dineros);
                IDCuentasNew = em.merge(IDCuentasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dineros.getIDDinero();
                if (findDineros(id) == null) {
                    throw new NonexistentEntityException("The dineros with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Dineros dineros;
            try {
                dineros = em.getReference(Dineros.class, id);
                dineros.getIDDinero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dineros with id " + id + " no longer exists.", enfe);
            }
            Cuentas IDCuentas = dineros.getIDCuentas();
            if (IDCuentas != null) {
                IDCuentas.getDinerosList().remove(dineros);
                IDCuentas = em.merge(IDCuentas);
            }
            em.remove(dineros);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dineros> findDinerosEntities() {
        return findDinerosEntities(true, -1, -1);
    }

    public List<Dineros> findDinerosEntities(int maxResults, int firstResult) {
        return findDinerosEntities(false, maxResults, firstResult);
    }

    private List<Dineros> findDinerosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dineros.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Dineros findDineros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dineros.class, id);
        } finally {
            em.close();
        }
    }

    public int getDinerosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dineros> rt = cq.from(Dineros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
     public List<Dineros> GetIdDinero() {

        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Dineros.findAll");
        List<Dineros> c = query.getResultList();
        return c;

    }
    
}
