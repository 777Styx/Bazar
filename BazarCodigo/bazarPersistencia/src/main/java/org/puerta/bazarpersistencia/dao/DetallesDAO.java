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
import jakarta.persistence.PersistenceException;
import java.util.List;
import org.puerta.bazarpersistencia.dominio.Detalle;
import org.puerta.bazarpersistencia.utils.JPAUtil;
import org.puerta.bazardependecias.excepciones.PersistenciaException;

public class DetallesDAO {

    /**
     * Método encargado de guardar un objeto `Detalle` en la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para persistir un objeto
     * `Detalle` en la base de datos dentro de una transacción. Si ocurre un
     * error durante el proceso de persistencia, se realiza un rollback en la
     * transacción y se lanza una excepción.
     *
     * @param detalle El objeto `Detalle` que se desea guardar en la base de
     * datos.
     * @throws PersistenciaException Si ocurre un error durante la operación de
     * persistencia, como un problema en la transacción o un error al intentar
     * guardar el objeto.
     */
    public void save(Detalle detalle) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Inicia una transacción
            em.getTransaction().begin();

            // Persiste el objeto detalle en la base de datos
            em.persist(detalle);

            // Si todo sale bien, realiza el commit de la transacción
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Si ocurre un error durante la transacción, realiza un rollback
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanza una excepción de persistencia con el mensaje de error y la causa original
            throw new PersistenciaException("Error al guardar el detalle", e);
        } finally {
            // Cierra el EntityManager para liberar recursos
            em.close();
        }
    }

    /**
     * Método encargado de buscar un objeto `Detalle` en la base de datos
     * utilizando su ID.
     *
     * Este método utiliza JPA (Java Persistence API) para realizar una búsqueda
     * de un objeto `Detalle` en la base de datos usando su identificador único
     * (ID). Si no se encuentra el detalle, se lanza una excepción.
     *
     * @param id El identificador único del objeto `Detalle` que se desea buscar
     * en la base de datos.
     * @return El objeto `Detalle` encontrado con el ID proporcionado.
     * @throws PersistenciaException Si no se encuentra el objeto `Detalle` con
     * el ID proporcionado o si ocurre un error durante la operación de
     * búsqueda.
     */
    public Detalle findById(Long id) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Busca el objeto Detalle en la base de datos usando el ID
            Detalle detalle = em.find(Detalle.class, id);

            // Si no se encuentra el detalle, lanza una excepción personalizada
            if (detalle == null) {
                throw new PersistenciaException("Detalle con ID " + id + " no encontrado");
            }

            // Si se encuentra el detalle, lo devuelve
            return detalle;
        } finally {
            // Cierra el EntityManager para liberar los recursos
            em.close();
        }
    }

    /**
     * Método encargado de actualizar un objeto `Detalle` en la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para realizar una
     * operación de actualización sobre un objeto `Detalle` en la base de datos.
     * Si ocurre un error durante la transacción, se maneja con una excepción y
     * se realiza un rollback.
     *
     * @param detalle El objeto `Detalle` que contiene los datos actualizados
     * que se desean guardar en la base de datos.
     * @throws PersistenciaException Si ocurre un error al intentar actualizar
     * el objeto `Detalle` en la base de datos o si ocurre un error durante la
     * transacción.
     */
    public void update(Detalle detalle) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Inicia una transacción para realizar la operación de actualización
            em.getTransaction().begin();

            // Realiza la operación de actualización del objeto `Detalle` en la base de datos
            em.merge(detalle);

            // Si todo es exitoso, confirma (commit) la transacción
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Si ocurre un error, se revierte (rollback) la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanza una excepción personalizada indicando el error de persistencia
            throw new PersistenciaException("Error al actualizar el detalle", e);
        } finally {
            // Cierra el EntityManager para liberar los recursos
            em.close();
        }
    }

    /**
     * Método encargado de eliminar un objeto `Detalle` de la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para realizar una
     * operación de eliminación sobre un objeto `Detalle` en la base de datos.
     * Si no se encuentra el detalle o si ocurre un error durante la
     * transacción, se maneja la excepción y se realiza un rollback para
     * revertir cualquier cambio realizado.
     *
     * @param id El identificador único del objeto `Detalle` que se desea
     * eliminar de la base de datos.
     * @throws PersistenciaException Si ocurre un error al intentar eliminar el
     * objeto `Detalle` de la base de datos, si no se encuentra el detalle con
     * el ID proporcionado, o si ocurre un error durante la transacción.
     */
    public void delete(Long id) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Inicia una transacción para realizar la operación de eliminación
            em.getTransaction().begin();

            // Busca el objeto Detalle en la base de datos utilizando el ID proporcionado
            Detalle detalle = em.find(Detalle.class, id);

            // Si no se encuentra el detalle, lanza una excepción personalizada
            if (detalle == null) {
                throw new PersistenciaException("Detalle con ID " + id + " no encontrado");
            }

            // Elimina el objeto Detalle de la base de datos
            em.remove(detalle);

            // Si todo es exitoso, confirma (commit) la transacción
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Si ocurre un error, se revierte (rollback) la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanza una excepción personalizada indicando el error de persistencia
            throw new PersistenciaException("Error al eliminar el detalle", e);
        } finally {
            // Cierra el EntityManager para liberar los recursos
            em.close();
        }
    }

    /**
     * Método encargado de obtener todos los objetos `Detalle` de la base de
     * datos.
     *
     * Este método utiliza JPA (Java Persistence API) para realizar una consulta
     * a la base de datos y obtener todos los objetos `Detalle` almacenados en
     * la tabla correspondiente. Si ocurre un error durante la operación, se
     * lanza una excepción personalizada.
     *
     * @return Una lista de objetos `Detalle` que representa todos los detalles
     * almacenados en la base de datos.
     * @throws PersistenciaException Si ocurre un error al intentar recuperar
     * los detalles de la base de datos.
     */
    public List<Detalle> findAll() throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Realiza una consulta JPQL para obtener todos los objetos Detalle de la base de datos
            return em.createQuery("SELECT d FROM Detalle d", Detalle.class).getResultList();
        } catch (PersistenceException e) {
            // Lanza una excepción personalizada si ocurre un error durante la consulta
            throw new PersistenciaException("Error al obtener todos los detalles", e);
        } finally {
            // Cierra el EntityManager para liberar los recursos
            em.close();
        }
    }

}
