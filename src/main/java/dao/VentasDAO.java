package dao;

import model.Venta;
import model.Cliente;
import model.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.NoResultException;
import java.util.List;

public class VentasDAO {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("libreriaPU");
    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final UsuariosDAO usuarioDAO = new UsuariosDAO();

    public void saveVenta(Venta venta) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.persist(venta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo guardar la venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Venta getVentaById(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery(
                "SELECT v FROM Venta v JOIN FETCH v.cliente JOIN FETCH v.usuario WHERE v.idVenta = :id", Venta.class)
                .setParameter("id", id)
                .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error al buscar venta por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando venta por ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void updateVenta(Venta venta) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.merge(venta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo actualizar la venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void deleteVenta(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            Venta venta = em.find(Venta.class, id);
            if (venta != null) {
                em.remove(venta);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo eliminar la venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Venta> getAllVentas() {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT v FROM Venta v JOIN FETCH v.cliente JOIN FETCH v.usuario", Venta.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener todas las ventas: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo ventas", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
        
    public Cliente getClienteReference(int id) {
        return clienteDAO.getClienteById(id);
    }
    
    public Usuario getUsuarioReference(int id) {
        return usuarioDAO.getUsuarioById(id);
    }
}