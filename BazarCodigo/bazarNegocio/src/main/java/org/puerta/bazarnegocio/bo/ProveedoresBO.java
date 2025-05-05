/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.List;
import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazarpersistencia.dominio.Proveedor;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.Convertor;
import org.puerta.bazarpersistencia.dao.ProveedoresDAO;

public class ProveedoresBO {

    private ProveedoresDAO proveedoresDAO = new ProveedoresDAO();

    /**
     * Método encargado de registrar un proveedor en el sistema.
     *
     * @param proveedorDTO El objeto `ProveedorDTO` que contiene la información
     * del proveedor a registrar.
     * @throws NegociosException Si ocurre un error al registrar el proveedor,
     * como que el nombre esté vacío o un error de persistencia.
     */
    public void registrarProveedor(ProveedorDTO proveedorDTO) throws NegociosException {
        try {
            // Verifica que el nombre del proveedor no esté vacío o sea nulo
            if (proveedorDTO.getNombre() == null || proveedorDTO.getNombre().trim().isEmpty()) {
                throw new NegociosException("El nombre del proveedor no puede estar vacío");
            }

            // Convierte el DTO a un objeto Proveedor
            Proveedor proveedor = Convertor.toProveedor(proveedorDTO);

            // Guarda el proveedor en la base de datos
            proveedoresDAO.save(proveedor);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al registrar el proveedor", e);
        }
    }

    /**
     * Método encargado de obtener un proveedor por su ID.
     *
     * @param id El ID del proveedor que se desea obtener.
     * @return Un objeto `ProveedorDTO` que contiene los datos del proveedor
     * obtenido.
     * @throws NegociosException Si ocurre un error al obtener el proveedor,
     * como que no se encuentre el proveedor o un error de persistencia.
     */
    public ProveedorDTO obtenerProveedorPorId(Long id) throws NegociosException {
        try {
            // Recupera el proveedor desde la base de datos usando el ID proporcionado
            Proveedor proveedor = proveedoresDAO.findById(id);

            // Si el proveedor no se encuentra, lanza una excepción NegociosException
            if (proveedor == null) {
                throw new NegociosException("Proveedor no encontrado");
            }

            // Convierte el proveedor obtenido a un DTO y lo retorna
            return Convertor.toProveedorDTO(proveedor);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al obtener el proveedor", e);
        }
    }

    /**
     * Método encargado de obtener todos los proveedores del sistema.
     *
     * @return Una lista de objetos `ProveedorDTO` que contiene los datos de
     * todos los proveedores disponibles.
     * @throws NegociosException Si ocurre un error al obtener los proveedores,
     * como un error de persistencia.
     */
    public List<ProveedorDTO> obtenerTodos() throws NegociosException {
        try {
            // Recupera todos los proveedores desde la base de datos
            List<Proveedor> proveedores = proveedoresDAO.findAll();

            // Convierte la lista de proveedores a una lista de DTOs y la retorna
            return Convertor.toProveedorDTOList(proveedores);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al obtener todos los proveedores", e);
        }
    }

    /**
     * Método encargado de encontrar un proveedor por su nombre.
     *
     * @param nombre El nombre del proveedor que se desea encontrar.
     * @return Un objeto `ProveedorDTO` que contiene los datos del proveedor
     * encontrado.
     * @throws NegociosException Si ocurre un error al encontrar el proveedor,
     * como que no se encuentre el proveedor con el nombre especificado.
     */
    public ProveedorDTO encontrarPorNombre(String nombre) throws NegociosException {
        // Crea una instancia de ProveedoresDAO para interactuar con la base de datos
        ProveedoresDAO dao = new ProveedoresDAO();

        // Recupera el proveedor de la base de datos usando el nombre proporcionado
        Proveedor entidad = dao.findByNombre(nombre);

        // Si no se encuentra el proveedor, lanza una excepción NegociosException
        if (entidad == null) {
            throw new NegociosException("Proveedor no encontrado");
        }

        // Convierte la entidad Proveedor a un DTO para ser retornado
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setCorreo(entidad.getCorreo());
        dto.setTelefono(entidad.getTelefono());
        dto.setDireccion(entidad.getDireccion());
        dto.setRepresentante(entidad.getRepresentante());

        // Retorna el DTO con la información del proveedor
        return dto;
    }

    /**
     * Método encargado de borrar un proveedor del sistema utilizando su ID.
     *
     * @param id El ID del proveedor que se desea eliminar.
     * @throws NegociosException Si ocurre un error al eliminar el proveedor,
     * como un error de persistencia al intentar borrar el registro.
     */
    public void borrarProveedor(Long id) throws NegociosException {
        try {
            // Elimina el proveedor de la base de datos utilizando el ID proporcionado
            proveedoresDAO.delete(id);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia al intentar eliminar el proveedor, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al eliminar el proveedor: " + e.getMessage(), e);
        }
    }

    /**
     * Método encargado de actualizar la información de un proveedor en el
     * sistema.
     *
     * @param proveedorDTO El objeto `ProveedorDTO` que contiene los datos
     * actualizados del proveedor.
     * @throws NegociosException Si ocurre un error al intentar actualizar el
     * proveedor, como un error de persistencia al realizar la actualización.
     */
    public void actualizarProveedor(ProveedorDTO proveedorDTO) throws NegociosException {
        try {
            // Convierte el DTO a una entidad Proveedor
            Proveedor proveedor = Convertor.toProveedor(proveedorDTO);

            // Actualiza la información del proveedor en la base de datos
            proveedoresDAO.update(proveedor);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al actualizar el proveedor: " + e.getMessage(), e);
        }
    }

}
