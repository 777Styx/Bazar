/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.puerta.bazarpersistencia.dominio.Usuario;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.JPAUtil;

public class UsuariosDAO {

    /**
     * Método encargado de guardar un nuevo usuario en la base de datos.
     *
     * Este método persiste un objeto `Usuario` en la base de datos. Se inicia
     * una transacción para asegurar la consistencia de la operación y se
     * persiste el objeto usando el EntityManager. Si ocurre un error durante la
     * persistencia, la transacción se revierte y se lanza una excepción
     * personalizada.
     *
     * @param usuario Objeto `Usuario` que será guardado en la base de datos.
     * @throws PersistenciaException Si ocurre un error al guardar el usuario en
     * la base de datos.
     */
    public void save(Usuario usuario) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar el usuario", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método que obtiene un usuario por su ID.
     *
     * Este método recupera un usuario de la base de datos usando su
     * identificador único (ID). Si el usuario no existe en la base de datos, se
     * lanza una excepción personalizada indicando que no se encontró el
     * usuario.
     *
     * @param id El ID del usuario a recuperar.
     * @return El objeto `Usuario` con el ID especificado.
     * @throws PersistenciaException Si ocurre un error al obtener el usuario
     * desde la base de datos.
     */
    public Usuario findById(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario == null) {
                throw new PersistenciaException("Usuario con ID " + id + " no encontrado");
            }
            return usuario;
        } finally {
            em.close();
        }
    }

    /**
     * Método que actualiza la información de un usuario existente.
     *
     * Este método permite actualizar un usuario en la base de datos. Si el
     * usuario no existe, la transacción no se ejecuta y se lanza una excepción
     * personalizada.
     *
     * @param usuario Objeto `Usuario` que contiene los nuevos datos a ser
     * actualizados.
     * @throws PersistenciaException Si ocurre un error al actualizar el usuario
     * en la base de datos.
     */
    public void update(Usuario usuario) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el usuario", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método encargado de eliminar un usuario por su ID.
     *
     * Este método elimina un usuario de la base de datos usando su
     * identificador único (ID). Si el usuario no existe, se lanza una excepción
     * personalizada.
     *
     * @param id El ID del usuario a eliminar.
     * @throws PersistenciaException Si ocurre un error al eliminar el usuario
     * de la base de datos.
     */
    public void delete(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario == null) {
                throw new PersistenciaException("Usuario con ID " + id + " no encontrado");
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el usuario", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método que obtiene todos los usuarios de la base de datos.
     *
     * Este método recupera todos los usuarios almacenados en la base de datos y
     * devuelve una lista de ellos. Si ocurre un error durante la ejecución de
     * la consulta, se lanza una excepción personalizada.
     *
     * @return Una lista de objetos `Usuario` que representa todos los usuarios
     * en la base de datos.
     * @throws PersistenciaException Si ocurre un error al obtener todos los
     * usuarios desde la base de datos.
     */
    public List<Usuario> findAll() throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al obtener todos los usuarios", e);
        } finally {
            em.close();
        }
    }

    /**
     * Método que busca un usuario por su nombre y contraseña.
     *
     * Este método busca un usuario en la base de datos usando el nombre y la
     * contraseña proporcionados. Si no se encuentra un usuario que coincida con
     * ambos, se lanza una excepción personalizada.
     *
     * @param nombre El nombre del usuario a buscar.
     * @param contrasena La contraseña del usuario a buscar.
     * @return El objeto `Usuario` que coincide con el nombre y la contraseña
     * proporcionados.
     * @throws PersistenciaException Si ocurre un error al buscar el usuario en
     * la base de datos o si no se encuentra.
     */
    public Usuario findByNameAndPassword(String nombre, String contrasena) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.contrasena = :contrasena", Usuario.class);
            query.setParameter("nombre", nombre);
            query.setParameter("contrasena", contrasena);

            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new PersistenciaException("Usuario no encontrado con nombre: " + nombre);
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al buscar el usuario por nombre y contraseña", e);
        } finally {
            em.close();
        }
    }

}
