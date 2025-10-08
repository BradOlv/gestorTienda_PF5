package dao;

import model.Venta;
import model.Cliente;
import model.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.NoResultException;
import java.util.List;
import model.DetalleVenta;

public class VentasDAO {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("libreriaPU");
    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final UsuariosDAO usuarioDAO = new UsuariosDAO(); 

    public void saveVenta(Venta venta) {
        // ... (Tu código actual para saveVenta es correcto) ...
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
    
    /**
     * Obtiene una venta por ID, asegurando que el Cliente y el Usuario sean cargados (FETCH JOIN).
     */
    public Venta getVentaById(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            // 💡 CORRECCIÓN: Se usa JOIN FETCH para cargar Cliente y Usuario en una sola consulta
            return em.createQuery(
                "SELECT v FROM Venta v JOIN FETCH v.cliente c JOIN FETCH v.usuario u WHERE v.idVenta = :id", Venta.class)
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
    
    // ... (updateVenta se mantiene igual) ...
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
    
    // ... (deleteVenta se mantiene igual, ya lo habías corregido) ...
    public void deleteVenta(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            
            Venta venta = em.find(Venta.class, id);
            
            if (venta != null) {
                // Eliminar los detalles primero (por clave foránea)
                em.createQuery("DELETE FROM DetalleVenta d WHERE d.venta.idVenta = :idVenta")
                    .setParameter("idVenta", id)
                    .executeUpdate();
                
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
    
    /**
     * Obtiene todas las ventas, asegurando que el Cliente y el Usuario sean cargados (FETCH JOIN).
     */
    public List<Venta> getAllVentas() {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            // 💡 CORRECCIÓN: Se usa JOIN FETCH para cargar Cliente y Usuario en una sola consulta
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
    
    // ... (El resto de métodos: getClienteReference, getUsuarioReference, guardarVentaCompleta, getDetallesByVentaId se mantienen igual) ...

    public Cliente getClienteReference(int id) {
        return clienteDAO.getClienteById(id);
    }
    
    public Usuario getUsuarioReference(int id) {
        return usuarioDAO.getUsuarioById(id);
    }
    
    public Venta guardarVentaCompleta(Venta venta, List<DetalleVenta> detalles) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();

            // 1. Persistir la Venta principal
            em.persist(venta);
            
            // 2. Persistir cada DetalleVenta y actualizar el stock
            for (DetalleVenta detalle : detalles) {
                // Asignar la Venta (con ID generado) al DetalleVenta
                detalle.setVenta(venta);
                em.persist(detalle);
                
                // 3. Actualizar el stock del producto
                model.Producto productoManaged = em.find(model.Producto.class, detalle.getProducto().getIdProducto());
                if (productoManaged != null) {
                    int nuevoStock = productoManaged.getStock() - detalle.getCantidad();
                    if (nuevoStock < 0) {
                           throw new RuntimeException("Stock insuficiente para el producto: " + productoManaged.getNombreProducto());
                    }
                    productoManaged.setStock(nuevoStock);
                    em.merge(productoManaged); // Actualiza el stock
                } else {
                       throw new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getIdProducto());
                }
            }

            em.getTransaction().commit();
            return venta; // Retorna la venta con su ID
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar venta y detalles: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en la transacción de la venta. Se hizo rollback. Detalle: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<DetalleVenta> getDetallesByVentaId(Integer idVenta) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery(
                "SELECT d FROM DetalleVenta d JOIN FETCH d.producto WHERE d.venta.idVenta = :idVenta", DetalleVenta.class)
                .setParameter("idVenta", idVenta)
                .getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener detalles de venta por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo detalles de venta", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}