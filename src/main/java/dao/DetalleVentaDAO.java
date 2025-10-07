package dao;

import model.DetalleVenta;
import model.Venta;
import model.Producto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author Kevin Velasquez
 */
public class DetalleVentaDAO {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("libreriaPU");
    private final VentasDAO ventaDAO = new VentasDAO(); 
    private final ProductosDAO productoDAO = new ProductosDAO(); 

    public void saveDetalleVenta(DetalleVenta detalleVenta) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.persist(detalleVenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar detalle de venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo guardar el detalle de venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public DetalleVenta getDetalleVentaById(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.find(DetalleVenta.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar detalle de venta por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando detalle de venta por ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void updateDetalleVenta(DetalleVenta detalleVenta) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.merge(detalleVenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar detalle de venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo actualizar el detalle de venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void deleteDetalleVenta(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            DetalleVenta detalleVenta = em.find(DetalleVenta.class, id);
            if (detalleVenta != null) {
                em.remove(detalleVenta);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar detalle de venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo eliminar el detalle de venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<DetalleVenta> getAllDetalleVentas() {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT d FROM DetalleVenta d", DetalleVenta.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los detalles de venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo detalles de venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<DetalleVenta> getDetallesByVenta(Integer idVenta) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT d FROM DetalleVenta d WHERE d.venta.idVenta = :idVenta", DetalleVenta.class)
                    .setParameter("idVenta", idVenta)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar detalles por venta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando detalles por venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<DetalleVenta> getDetallesByProducto(Integer idProducto) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT d FROM DetalleVenta d WHERE d.producto.idProducto = :idProducto", DetalleVenta.class)
                    .setParameter("idProducto", idProducto)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar detalles por producto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando detalles por producto", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Venta getVentaReference(int id) {
        return ventaDAO.getVentaById(id);
    }
    
    public Producto getProductoReference(int id) {
        return productoDAO.getProductoById(id); 
    }
}