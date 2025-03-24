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

    public void registrarProveedor(ProveedorDTO proveedorDTO) throws NegociosException {
        try {
            if (proveedorDTO.getNombre() == null || proveedorDTO.getNombre().trim().isEmpty()) {
                throw new NegociosException("El nombre del proveedor no puede estar vacío");
            }

            Proveedor proveedor = Convertor.toProveedor(proveedorDTO);

            proveedoresDAO.save(proveedor);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el proveedor", e);
        }
    }

    public ProveedorDTO obtenerProveedorPorId(Long id) throws NegociosException {
        try {
            Proveedor proveedor = proveedoresDAO.findById(id);
            if (proveedor == null) {
                throw new NegociosException("Proveedor no encontrado");
            }

            return Convertor.toProveedorDTO(proveedor);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el proveedor", e);
        }
    }

    public List<ProveedorDTO> obtenerTodos() throws NegociosException {
        try {
            List<Proveedor> proveedores = proveedoresDAO.findAll();
            return Convertor.toProveedorDTOList(proveedores);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener todos los proveedores", e);
        }
    }
}
