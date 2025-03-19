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

    public void registrarDetalle(DetalleDTO detalleDTO) throws NegociosException {
        try {
            if (detalleDTO.getImporte() < 0) {
                throw new NegociosException("El importe del detalle no puede ser negativo");
            }

            Detalle detalle = Convertor.toDetalle(detalleDTO, null, null); // Venta y Producto se pueden asignar después

            detallesDAO.save(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el detalle", e);
        }
    }

    public DetalleDTO obtenerDetallePorId(Long id) throws NegociosException {
        try {
            Detalle detalle = detallesDAO.findById(id);
            if (detalle == null) {
                throw new NegociosException("Detalle no encontrado");
            }

            return Convertor.toDetalleDTO(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el detalle", e);
        }
    }
}
