/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazardependecias.dominio.Usuario;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazardependecias.utils.JPAUtil;

public class UsuariosDAO {

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
}