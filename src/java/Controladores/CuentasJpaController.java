/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.RollbackFailureException;
import Entidades.Cuentas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Usuarios;
import Entidades.Dineros;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author xcojcama
 */
public class CuentasJpaController implements Serializable {

    public CuentasJpaController() {
       this.emf = Persistence.createEntityManagerFactory("Banco_SocketPU");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuentas cuentas) throws RollbackFailureException, Exception {
        if (cuentas.getDinerosList() == null) {
            cuentas.setDinerosList(new ArrayList<Dineros>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios IDUsuarios = cuentas.getIDUsuarios();
            if (IDUsuarios != null) {
                IDUsuarios = em.getReference(IDUsuarios.getClass(), IDUsuarios.getIDUsuarios());
                cuentas.setIDUsuarios(IDUsuarios);
            }
            List<Dineros> attachedDinerosList = new ArrayList<Dineros>();
            for (Dineros dinerosListDinerosToAttach : cuentas.getDinerosList()) {
                dinerosListDinerosToAttach = em.getReference(dinerosListDinerosToAttach.getClass(), dinerosListDinerosToAttach.getIDDinero());
                attachedDinerosList.add(dinerosListDinerosToAttach);
            }
            cuentas.setDinerosList(attachedDinerosList);
            em.persist(cuentas);
            if (IDUsuarios != null) {
                IDUsuarios.getCuentasList().add(cuentas);
                IDUsuarios = em.merge(IDUsuarios);
            }
            for (Dineros dinerosListDineros : cuentas.getDinerosList()) {
                Cuentas oldIDCuentasOfDinerosListDineros = dinerosListDineros.getIDCuentas();
                dinerosListDineros.setIDCuentas(cuentas);
                dinerosListDineros = em.merge(dinerosListDineros);
                if (oldIDCuentasOfDinerosListDineros != null) {
                    oldIDCuentasOfDinerosListDineros.getDinerosList().remove(dinerosListDineros);
                    oldIDCuentasOfDinerosListDineros = em.merge(oldIDCuentasOfDinerosListDineros);
                }
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

    public void edit(Cuentas cuentas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuentas persistentCuentas = em.find(Cuentas.class, cuentas.getIDCuentas());
            Usuarios IDUsuariosOld = persistentCuentas.getIDUsuarios();
            Usuarios IDUsuariosNew = cuentas.getIDUsuarios();
            List<Dineros> dinerosListOld = persistentCuentas.getDinerosList();
            List<Dineros> dinerosListNew = cuentas.getDinerosList();
            List<String> illegalOrphanMessages = null;
            for (Dineros dinerosListOldDineros : dinerosListOld) {
                if (!dinerosListNew.contains(dinerosListOldDineros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dineros " + dinerosListOldDineros + " since its IDCuentas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (IDUsuariosNew != null) {
                IDUsuariosNew = em.getReference(IDUsuariosNew.getClass(), IDUsuariosNew.getIDUsuarios());
                cuentas.setIDUsuarios(IDUsuariosNew);
            }
            List<Dineros> attachedDinerosListNew = new ArrayList<Dineros>();
            for (Dineros dinerosListNewDinerosToAttach : dinerosListNew) {
                dinerosListNewDinerosToAttach = em.getReference(dinerosListNewDinerosToAttach.getClass(), dinerosListNewDinerosToAttach.getIDDinero());
                attachedDinerosListNew.add(dinerosListNewDinerosToAttach);
            }
            dinerosListNew = attachedDinerosListNew;
            cuentas.setDinerosList(dinerosListNew);
            cuentas = em.merge(cuentas);
            if (IDUsuariosOld != null && !IDUsuariosOld.equals(IDUsuariosNew)) {
                IDUsuariosOld.getCuentasList().remove(cuentas);
                IDUsuariosOld = em.merge(IDUsuariosOld);
            }
            if (IDUsuariosNew != null && !IDUsuariosNew.equals(IDUsuariosOld)) {
                IDUsuariosNew.getCuentasList().add(cuentas);
                IDUsuariosNew = em.merge(IDUsuariosNew);
            }
            for (Dineros dinerosListNewDineros : dinerosListNew) {
                if (!dinerosListOld.contains(dinerosListNewDineros)) {
                    Cuentas oldIDCuentasOfDinerosListNewDineros = dinerosListNewDineros.getIDCuentas();
                    dinerosListNewDineros.setIDCuentas(cuentas);
                    dinerosListNewDineros = em.merge(dinerosListNewDineros);
                    if (oldIDCuentasOfDinerosListNewDineros != null && !oldIDCuentasOfDinerosListNewDineros.equals(cuentas)) {
                        oldIDCuentasOfDinerosListNewDineros.getDinerosList().remove(dinerosListNewDineros);
                        oldIDCuentasOfDinerosListNewDineros = em.merge(oldIDCuentasOfDinerosListNewDineros);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cuentas.getIDCuentas();
                if (findCuentas(id) == null) {
                    throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuentas cuentas;
            try {
                cuentas = em.getReference(Cuentas.class, id);
                cuentas.getIDCuentas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Dineros> dinerosListOrphanCheck = cuentas.getDinerosList();
            for (Dineros dinerosListOrphanCheckDineros : dinerosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuentas (" + cuentas + ") cannot be destroyed since the Dineros " + dinerosListOrphanCheckDineros + " in its dinerosList field has a non-nullable IDCuentas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios IDUsuarios = cuentas.getIDUsuarios();
            if (IDUsuarios != null) {
                IDUsuarios.getCuentasList().remove(cuentas);
                IDUsuarios = em.merge(IDUsuarios);
            }
            em.remove(cuentas);
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

    public List<Cuentas> findCuentasEntities() {
        return findCuentasEntities(true, -1, -1);
    }

    public List<Cuentas> findCuentasEntities(int maxResults, int firstResult) {
        return findCuentasEntities(false, maxResults, firstResult);
    }

    private List<Cuentas> findCuentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuentas.class));
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

    public Cuentas findCuentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuentas> rt = cq.from(Cuentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public int IdCuentas(String cuenta) {

        EntityManager em = getEntityManager();

        Query query = em.createNamedQuery("Cuentas.findByNumerocuenta");
        query.setParameter("numerocuenta", cuenta);
        Cuentas c = (Cuentas) query.getSingleResult();
        int idCuenta = c.getIDCuentas();
        return idCuenta;
    }
    
}
