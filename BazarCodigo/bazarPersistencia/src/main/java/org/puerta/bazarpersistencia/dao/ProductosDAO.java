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

    /**
     * Método encargado de guardar un objeto `Producto` en la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para realizar una
     * operación de persistencia y almacenar el objeto `Producto` en la base de
     * datos. Si ocurre un error durante el proceso, la transacción es revertida
     * y se lanza una excepción personalizada.
     *
     * @param producto El objeto `Producto` que se desea guardar en la base de
     * datos.
     * @throws PersistenciaException Si ocurre un error al intentar guardar el
     * producto en la base de datos.
     */
    public void save(Producto producto) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Inicia una nueva transacción para asegurar la operación atómica
            em.getTransaction().begin();

            // Realiza la operación de persistencia, guardando el objeto Producto en la base de datos
            em.persist(producto);

            // Si la persistencia fue exitosa, se confirma la transacción
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Si ocurre un error, se revierte la transacción si está activa
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            // Lanza una excepción personalizada con el mensaje de error y la causa original
            throw new PersistenciaException("Error al guardar el producto", e);
        } finally {
            // Cierra el EntityManager para liberar los recursos asociados
            em.close();
        }
    }

    /**
     * Método encargado de encontrar un producto por su ID en la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para buscar un objeto
     * `Producto` en la base de datos utilizando su identificador único (ID). Si
     * no se encuentra el producto, se lanza una excepción personalizada.
     *
     * @param id El ID del producto que se desea encontrar.
     * @return El objeto `Producto` correspondiente al ID proporcionado.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda del
     * producto en la base de datos, o si no se encuentra el producto con el ID
     * especificado.
     */
    public Producto findById(Long id) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Busca el producto en la base de datos utilizando su ID
            Producto producto = em.find(Producto.class, id);

            // Si no se encuentra el producto con el ID proporcionado, lanza una excepción
            if (producto == null) {
                throw new PersistenciaException("Producto con ID " + id + " no encontrado");
            }

            // Devuelve el producto encontrado
            return producto;
        } finally {
            // Cierra el EntityManager para liberar los recursos asociados
            em.close();
        }
    }

    /**
     * Método encargado de actualizar un producto en la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para realizar una
     * operación de actualización (merge) de un objeto `Producto` en la base de
     * datos. Si ocurre un error durante la actualización, se maneja la
     * transacción de manera adecuada, asegurando que no queden datos
     * inconsistentes.
     *
     * @param producto El objeto `Producto` que se desea actualizar en la base
     * de datos.
     * @throws PersistenciaException Si ocurre un error durante la operación de
     * actualización en la base de datos.
     */
    public void update(Producto producto) throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Inicia una transacción en la base de datos
            em.getTransaction().begin();

            // Realiza la actualización del objeto Producto en la base de datos
            em.merge(producto);

            // Confirma los cambios realizados en la base de datos
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            // Si ocurre un error, se revierte la transacción para evitar dejar datos inconsistentes
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            // Lanza una excepción personalizada para manejar los errores de persistencia
            throw new PersistenciaException("Error al actualizar el producto", e);
        } finally {
            // Cierra el EntityManager para liberar los recursos utilizados
            em.close();
        }
    }

    /**
     *
     * @param id
     * @throws PersistenciaException
     */
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

    /**
     * Método encargado de obtener todos los productos de la base de datos.
     *
     * Este método utiliza JPA (Java Persistence API) para ejecutar una consulta
     * que recupera todos los registros de la entidad `Producto` desde la base
     * de datos. Si ocurre un error durante la operación de recuperación, se
     * maneja de manera adecuada, garantizando que se manejen las excepciones de
     * persistencia correctamente.
     *
     * @return Una lista de objetos `Producto` que representa todos los
     * productos en la base de datos.
     * @throws PersistenciaException Si ocurre un error al ejecutar la consulta
     * o al obtener los resultados.
     */
    public List<Producto> findAll() throws PersistenciaException {
        // Obtiene el EntityManager para interactuar con la base de datos
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Ejecuta la consulta que obtiene todos los productos de la base de datos
            return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        } catch (PersistenceException e) {
            // Lanza una excepción personalizada si ocurre un error en la consulta
            throw new PersistenciaException("Error al obtener todos los productos", e);
        } finally {
            // Cierra el EntityManager para liberar los recursos utilizados
            em.close();
        }
    }

}
