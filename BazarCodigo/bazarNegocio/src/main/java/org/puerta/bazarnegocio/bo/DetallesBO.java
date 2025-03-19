/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import org.puerta.bazardependecias.dominio.Detalle;
import org.puerta.bazardependecias.dto.DetalleDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazardependecias.utils.Convertor;
import org.puerta.bazarpersistencia.dao.DetallesDAO;

/**
 *
 * @author olive
 */
public class DetallesBO {
    private DetallesDAO detallesDAO = new DetallesDAO();

    // Método para registrar un detalle
    public void registrarDetalle(DetalleDTO detalleDTO) throws NegociosException {
        try {
            // Validar que el importe no sea negativo
            if (detalleDTO.getImporte() < 0) {
                throw new NegociosException("El importe del detalle no puede ser negativo");
            }

            // Convertir DTO a entidad
            Detalle detalle = Convertor.toDetalle(detalleDTO, null, null); // Venta y Producto se pueden asignar después

            // Guardar el detalle
            detallesDAO.save(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el detalle", e);
        }
    }

    // Método para obtener un detalle por ID
    public DetalleDTO obtenerDetallePorId(Long id) throws NegociosException {
        try {
            Detalle detalle = detallesDAO.findById(id);
            if (detalle == null) {
                throw new NegociosException("Detalle no encontrado");
            }

            // Convertir entidad a DTO
            return Convertor.toDetalleDTO(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el detalle", e);
        }
    }
}
