/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazarpersistencia.dominio.Producto;
import org.puerta.bazarpersistencia.utils.JPAUtil;
import org.puerta.bazardependecias.excepciones.PersistenciaException;

public class ProductosDAO {

    public void save(Producto producto) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar el producto", e);
        } finally {
            em.close();
        }
    }

    public Producto findById(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Producto producto = em.find(Producto.class, id);
            if (producto == null) {
                throw new PersistenciaException("Producto con ID " + id + " no encontrado");
            }
            return producto;
        } finally {
            em.close();
        }
    }

    public void update(Producto producto) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el producto", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto == null) {
                throw new PersistenciaException("Producto con ID " + id + " no encontrado");
            }
            em.remove(producto);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el producto", e);
        } finally {
            em.close();
        }
    }

    public List<Producto> findAll() throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al obtener todos los productos", e);
        } finally {
            em.close();
        }
    }
}