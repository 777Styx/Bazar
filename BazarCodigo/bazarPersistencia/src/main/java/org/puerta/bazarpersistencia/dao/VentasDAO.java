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

    /**
     * Método encargado de guardar una nueva venta en la base de datos.
     *
     * Este método persiste un objeto `Venta` en la base de datos. Se inicia una
     * transacción para asegurar la consistencia de la operación y se persiste
     * el objeto usando el EntityManager. Si ocurre un error durante la
     * persistencia, la transacción se revierte y se lanza una excepción
     * personalizada.
     *
     * @param venta Objeto `Venta` que será guardado en la base de datos.
     * @throws PersistenciaException Si ocurre un error al guardar la venta en
     * la base de datos.
     */
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

    /**
     * Método que obtiene una venta por su ID.
     *
     * Este método recupera una venta de la base de datos usando su
     * identificador único (ID). Si la venta no existe en la base de datos, se
     * lanza una excepción personalizada indicando que no se encontró la venta.
     * Además, se asegura de cargar los detalles de la venta (relación con la
     * entidad `Detalle`).
     *
     * @param id El ID de la venta a recuperar.
     * @return El objeto `Venta` con el ID especificado.
     * @throws PersistenciaException Si ocurre un error al obtener la venta
     * desde la base de datos.
     */
    public Venta findById(Long id) throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Venta venta = em.find(Venta.class, id);
            if (venta == null) {
                throw new PersistenciaException("Venta con ID " + id + " no encontrada");
            }

            // Asegura que los detalles de la venta estén cargados (evita LazyLoadingException).
            venta.getDetalles().size();
            return venta;
        } finally {
            em.close();
        }
    }

    /**
     * Método que actualiza la información de una venta existente.
     *
     * Este método permite actualizar una venta en la base de datos. Si la venta
     * no existe, la transacción no se ejecuta y se lanza una excepción
     * personalizada.
     *
     * @param venta Objeto `Venta` que contiene los nuevos datos a ser
     * actualizados.
     * @throws PersistenciaException Si ocurre un error al actualizar la venta
     * en la base de datos.
     */
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

    /**
     * Método encargado de eliminar una venta por su ID.
     *
     * Este método elimina una venta de la base de datos usando su identificador
     * único (ID). Si la venta no existe, se lanza una excepción personalizada.
     * La transacción se confirma después de eliminar la venta, o se revierte en
     * caso de error.
     *
     * @param id El ID de la venta a eliminar.
     * @throws PersistenciaException Si ocurre un error al eliminar la venta de
     * la base de datos.
     */
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

    /**
     * Método que obtiene todas las ventas de la base de datos.
     *
     * Este método recupera todas las ventas almacenadas en la base de datos y
     * devuelve una lista de ellas. Además, se asegura de cargar los detalles de
     * cada venta para evitar problemas con la carga perezosa (LazyLoading). Si
     * ocurre un error durante la ejecución de la consulta, se lanza una
     * excepción personalizada.
     *
     * @return Una lista de objetos `Venta` que representa todas las ventas en
     * la base de datos.
     * @throws PersistenciaException Si ocurre un error al obtener todas las
     * ventas desde la base de datos.
     */
    public List<Venta> findAll() throws PersistenciaException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Venta> ventas = em.createQuery("SELECT v FROM Venta v", Venta.class).getResultList();

            // Asegura que los detalles de cada venta estén cargados (evita LazyLoadingException).
            for (Venta venta : ventas) {
                venta.getDetalles().size();
            }

            return ventas;
        } catch (PersistenceException e) {
            throw new PersistenciaException("Error al obtener todas las ventas", e);
        } finally {
            em.close();
        }
    }

}
