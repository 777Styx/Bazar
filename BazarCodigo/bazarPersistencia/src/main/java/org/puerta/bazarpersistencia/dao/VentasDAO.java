/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazarpersistencia.dominio.Venta;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.JPAUtil;

public class VentasDAO {

    public void save(Venta venta) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(venta);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar la venta", e);
        } finally {
            em.close();
        }
    }

    public Venta findById(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Venta venta = em.find(Venta.class, id);
            if (venta == null) {
                throw new PersistenciaException("Venta con ID " + id + " no encontrada");
            }
            return venta;
        } finally {
            em.close();
        }
    }

    public void update(Venta venta) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(venta);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar la venta", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Venta venta = em.find(Venta.class, id);
            if (venta == null) {
                throw new PersistenciaException("Venta con ID " + id + " no encontrada");
            }
            em.remove(venta);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar la venta", e);
        } finally {
            em.close();
        }
    }

    public List<Venta> findAll() throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT v FROM Venta v", Venta.class).getResultList();
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al obtener todas las ventas", e);
        } finally {
            em.close();
        }
    }
}
