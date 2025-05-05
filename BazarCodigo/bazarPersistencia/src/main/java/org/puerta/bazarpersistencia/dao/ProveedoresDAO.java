/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazarpersistencia.dominio.Proveedor;
import org.puerta.bazarpersistencia.utils.JPAUtil;
import org.puerta.bazardependecias.excepciones.PersistenciaException;

public class ProveedoresDAO {

    /**
     * Método encargado de guardar un nuevo proveedor en la base de datos.
     *
     * Este método persiste un objeto `Proveedor` en la base de datos. Se inicia
     * una transacción para asegurar la consistencia de la operación y se
     * persiste el objeto usando el EntityManager. Si ocurre un error durante la
     * persistencia, la transacción se revierte y se lanza una excepción
     * personalizada.
     *
     * @param proveedor Objeto `Proveedor` que será guardado en la base de
     * datos.
     * @throws PersistenciaException Si ocurre un error al guardar el proveedor
     * en la base de datos.
     */
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

    /**
     * Método que obtiene un proveedor por su ID.
     *
     * Este método recupera un proveedor de la base de datos usando su
     * identificador único (ID). Si el proveedor no existe en la base de datos,
     * se lanza una excepción personalizada indicando que no se encontró el
     * proveedor.
     *
     * @param id El ID del proveedor a recuperar.
     * @return El objeto `Proveedor` con el ID especificado.
     * @throws PersistenciaException Si ocurre un error al obtener el proveedor
     * desde la base de datos.
     */
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

    /**
     * Método que actualiza la información de un proveedor existente.
     *
     * Este método permite actualizar un proveedor en la base de datos. Si el
     * proveedor no existe, la transacción no se ejecuta y se lanza una
     * excepción personalizada.
     *
     * @param proveedor Objeto `Proveedor` que contiene los nuevos datos a ser
     * actualizados.
     * @throws PersistenciaException Si ocurre un error al actualizar el
     * proveedor en la base de datos.
     */
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

    /**
     * Método encargado de eliminar un proveedor por su ID.
     *
     * Este método elimina un proveedor de la base de datos usando su
     * identificador único (ID). Si el proveedor no existe, se lanza una
     * excepción personalizada.
     *
     * @param id El ID del proveedor a eliminar.
     * @throws PersistenciaException Si ocurre un error al eliminar el proveedor
     * de la base de datos.
     */
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

    /**
     * Método que obtiene todos los proveedores de la base de datos.
     *
     * Este método recupera todos los proveedores almacenados en la base de
     * datos y devuelve una lista de ellos. Si ocurre un error durante la
     * ejecución de la consulta, se lanza una excepción personalizada.
     *
     * @return Una lista de objetos `Proveedor` que representa todos los
     * proveedores en la base de datos.
     * @throws PersistenciaException Si ocurre un error al obtener todos los
     * proveedores desde la base de datos.
     */
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

    /**
     * Método que busca un proveedor por su nombre.
     *
     * Este método recupera un proveedor de la base de datos usando su nombre.
     * Si el proveedor no se encuentra, se retorna `null` y no se lanza una
     * excepción.
     *
     * @param nombre El nombre del proveedor a buscar.
     * @return El objeto `Proveedor` con el nombre especificado, o `null` si no
     * se encuentra.
     */
    public Proveedor findByNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Proveedor p WHERE p.nombre = :nombre", Proveedor.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
