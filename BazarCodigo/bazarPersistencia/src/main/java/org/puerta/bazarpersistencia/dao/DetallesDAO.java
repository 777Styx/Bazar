/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

/**
 *
 * @author olive
 */
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazarpersistencia.dominio.Detalle;
import org.puerta.bazarpersistencia.utils.JPAUtil;
import org.puerta.bazardependecias.excepciones.PersistenciaException;

public class DetallesDAO {

    public void save(Detalle detalle) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(detalle);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar el detalle", e);
        } finally {
            em.close();
        }
    }

    public Detalle findById(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Detalle detalle = em.find(Detalle.class, id);
            if (detalle == null) {
                throw new PersistenciaException("Detalle con ID " + id + " no encontrado");
            }
            return detalle;
        } finally {
            em.close();
        }
    }

    public void update(Detalle detalle) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(detalle);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el detalle", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Detalle detalle = em.find(Detalle.class, id);
            if (detalle == null) {
                throw new PersistenciaException("Detalle con ID " + id + " no encontrado");
            }
            em.remove(detalle);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el detalle", e);
        } finally {
            em.close();
        }
    }

    public List<Detalle> findAll() throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Detalle d", Detalle.class).getResultList();
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al obtener todos los detalles", e);
        } finally {
            em.close();
        }
    }
}