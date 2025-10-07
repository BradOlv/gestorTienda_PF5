package dao;

import model.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Date;

import javax.persistence.NoResultException;

/**
 *
 * @author Bradley Oliva
 */
public class UsuariosDAO {
    // Asegúrate de que "libreriaPU" coincida con el nombre en tu persistence.xml
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("libreriaPU");

    public void saveUsuario(Usuario usuario) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            // Si la fecha de registro no está seteada, la seteamos antes de persistir
            if (usuario.getFechaRegistro() == null) {
                usuario.setFechaRegistro(new Date());
            }
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo guardar el usuario", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Usuario getUsuarioById(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.find(Usuario.class, id);
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando usuario por ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void updateUsuario(Usuario usuario) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo actualizar el usuario", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public void deleteUsuario(Integer id) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo eliminar el usuario", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    
    public List<Usuario> getAllUsuarios() {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo usuarios", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Usuario getUserByEmail(String email) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            return em.createQuery("SELECT u FROM Usuario u WHERE u.emailUsuario = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error buscando usuario por email", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}