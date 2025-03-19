/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.List;
import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazardependecias.dominio.Proveedor;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazardependecias.utils.Convertor;
import org.puerta.bazarpersistencia.dao.ProveedoresDAO;

public class ProveedoresBO {

    private ProveedoresDAO proveedoresDAO = new ProveedoresDAO();

    // Método para registrar un proveedor
    public void registrarProveedor(ProveedorDTO proveedorDTO) throws NegociosException {
        try {
            // Validar que el nombre no esté vacío
            if (proveedorDTO.getNombre() == null || proveedorDTO.getNombre().trim().isEmpty()) {
                throw new NegociosException("El nombre del proveedor no puede estar vacío");
            }

            // Convertir DTO a entidad
            Proveedor proveedor = Convertor.toProveedor(proveedorDTO);

            // Guardar el proveedor
            proveedoresDAO.save(proveedor);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el proveedor", e);
        }
    }

    // Método para obtener un proveedor por ID
    public ProveedorDTO obtenerProveedorPorId(Long id) throws NegociosException {
        try {
            Proveedor proveedor = proveedoresDAO.findById(id);
            if (proveedor == null) {
                throw new NegociosException("Proveedor no encontrado");
            }

            // Convertir entidad a DTO
            return Convertor.toProveedorDTO(proveedor);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el proveedor", e);
        }
    }

    // Método para obtener todos los proveedores
    public List<ProveedorDTO> obtenerTodos() throws NegociosException {
        try {
            List<Proveedor> proveedores = proveedoresDAO.findAll();
            return Convertor.toProveedorDTOList(proveedores);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener todos los proveedores", e);
        }
    }
}
