/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Cuentas;
import Entidades.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author xcojcama
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws RollbackFailureException, Exception {
        if (usuarios.getCuentasList() == null) {
            usuarios.setCuentasList(new ArrayList<Cuentas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Cuentas> attachedCuentasList = new ArrayList<Cuentas>();
            for (Cuentas cuentasListCuentasToAttach : usuarios.getCuentasList()) {
                cuentasListCuentasToAttach = em.getReference(cuentasListCuentasToAttach.getClass(), cuentasListCuentasToAttach.getIDCuentas());
                attachedCuentasList.add(cuentasListCuentasToAttach);
            }
            usuarios.setCuentasList(attachedCuentasList);
            em.persist(usuarios);
            for (Cuentas cuentasListCuentas : usuarios.getCuentasList()) {
                Usuarios oldIDUsuariosOfCuentasListCuentas = cuentasListCuentas.getIDUsuarios();
                cuentasListCuentas.setIDUsuarios(usuarios);
                cuentasListCuentas = em.merge(cuentasListCuentas);
                if (oldIDUsuariosOfCuentasListCuentas != null) {
                    oldIDUsuariosOfCuentasListCuentas.getCuentasList().remove(cuentasListCuentas);
                    oldIDUsuariosOfCuentasListCuentas = em.merge(oldIDUsuariosOfCuentasListCuentas);
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

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIDUsuarios());
            List<Cuentas> cuentasListOld = persistentUsuarios.getCuentasList();
            List<Cuentas> cuentasListNew = usuarios.getCuentasList();
            List<String> illegalOrphanMessages = null;
            for (Cuentas cuentasListOldCuentas : cuentasListOld) {
                if (!cuentasListNew.contains(cuentasListOldCuentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuentas " + cuentasListOldCuentas + " since its IDUsuarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cuentas> attachedCuentasListNew = new ArrayList<Cuentas>();
            for (Cuentas cuentasListNewCuentasToAttach : cuentasListNew) {
                cuentasListNewCuentasToAttach = em.getReference(cuentasListNewCuentasToAttach.getClass(), cuentasListNewCuentasToAttach.getIDCuentas());
                attachedCuentasListNew.add(cuentasListNewCuentasToAttach);
            }
            cuentasListNew = attachedCuentasListNew;
            usuarios.setCuentasList(cuentasListNew);
            usuarios = em.merge(usuarios);
            for (Cuentas cuentasListNewCuentas : cuentasListNew) {
                if (!cuentasListOld.contains(cuentasListNewCuentas)) {
                    Usuarios oldIDUsuariosOfCuentasListNewCuentas = cuentasListNewCuentas.getIDUsuarios();
                    cuentasListNewCuentas.setIDUsuarios(usuarios);
                    cuentasListNewCuentas = em.merge(cuentasListNewCuentas);
                    if (oldIDUsuariosOfCuentasListNewCuentas != null && !oldIDUsuariosOfCuentasListNewCuentas.equals(usuarios)) {
                        oldIDUsuariosOfCuentasListNewCuentas.getCuentasList().remove(cuentasListNewCuentas);
                        oldIDUsuariosOfCuentasListNewCuentas = em.merge(oldIDUsuariosOfCuentasListNewCuentas);
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
                Integer id = usuarios.getIDUsuarios();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIDUsuarios();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cuentas> cuentasListOrphanCheck = usuarios.getCuentasList();
            for (Cuentas cuentasListOrphanCheckCuentas : cuentasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Cuentas " + cuentasListOrphanCheckCuentas + " in its cuentasList field has a non-nullable IDUsuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
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

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
