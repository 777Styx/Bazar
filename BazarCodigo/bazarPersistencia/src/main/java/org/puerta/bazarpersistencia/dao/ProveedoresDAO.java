/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazardependecias.dominio.Proveedor;
import org.puerta.bazardependecias.utils.JPAUtil;
import org.puerta.bazardependecias.excepciones.PersistenciaException;

public class ProveedoresDAO {

    public void save(Proveedor proveedor) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(proveedor);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar el proveedor", e);
        } finally {
            em.close();
        }
    }

    public Proveedor findById(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Proveedor proveedor = em.find(Proveedor.class, id);
            if (proveedor == null) {
                throw new PersistenciaException("Proveedor con ID " + id + " no encontrado");
            }
            return proveedor;
        } finally {
            em.close();
        }
    }

    public void update(Proveedor proveedor) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(proveedor);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el proveedor", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Proveedor proveedor = em.find(Proveedor.class, id);
            if (proveedor == null) {
                throw new PersistenciaException("Proveedor con ID " + id + " no encontrado");
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el proveedor", e);
        } finally {
            em.close();
        }
    }

    public List<Proveedor> findAll() throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Proveedor p", Proveedor.class).getResultList();
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al obtener todos los proveedores", e);
        } finally {
            em.close();
        }
    }
}
