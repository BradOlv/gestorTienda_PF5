package dao;

import model.Producto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.NoResultException;

/**
 *
 * @author Kevin Velasquez
 */
public class ProductosDAO {
    // Asegúrate de que "libreriaPU" coincida con el nombre en tu persistence.xml
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("libreriaPU");

    public void saveProducto(Producto producto) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            // Si la fecha de creación no está seteada, la seteamos antes de persistir
            if (producto.getFechaCreacion() == null) {
                producto.setFechaCreacion(Timestamp.from(Instant.now()));
            }
            em.persist(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo guardar el producto", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Producto getProductoById(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.find(Producto.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando producto por ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void updateProducto(Producto producto) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo actualizar el producto", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void deleteProducto(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null) {
                em.remove(producto);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo eliminar el producto", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Producto> getAllProductos() {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo productos", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Producto> getProductosByCategoria(String categoria) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT p FROM Producto p WHERE p.categoria = :categoria", Producto.class)
                    .setParameter("categoria", categoria)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando productos por categoría", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Producto> getProductosByMarca(String marca) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT p FROM Producto p WHERE p.marca = :marca", Producto.class)
                    .setParameter("marca", marca)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar productos por marca: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando productos por marca", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Producto> getProductosByNombre(String nombre) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT p FROM Producto p WHERE p.nombreProducto LIKE :nombre", Producto.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando productos por nombre", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}