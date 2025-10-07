package dao;

import model.Cliente;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 *
 * @author Bradley Oliva
 */
public class ClientesDAO {
    // Asegúrate de que "libreriaPU" coincida con el nombre en tu persistence.xml
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("libreriaPU");

    public void saveCliente(Cliente cliente) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar cliente: " + e.getMessage());
            throw new RuntimeException("No se pudo guardar el cliente", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Cliente getClienteById(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.find(Cliente.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
            throw new RuntimeException("Error buscando cliente por ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void updateCliente(Cliente cliente) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            throw new RuntimeException("No se pudo actualizar el cliente", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void deleteCliente(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar el cliente", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Cliente> getAllClientes() {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los clientes: " + e.getMessage());
            throw new RuntimeException("Error obteniendo clientes", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}