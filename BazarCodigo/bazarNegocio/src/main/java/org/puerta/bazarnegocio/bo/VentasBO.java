/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.List;
import org.puerta.bazardependecias.dto.VentaDTO;
import org.puerta.bazarpersistencia.dominio.Venta;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.Convertor;
import org.puerta.bazarpersistencia.dao.VentasDAO;

public class VentasBO {

    private VentasDAO ventaDAO = new VentasDAO();

    public void registrarVenta(VentaDTO ventaDTO) throws NegociosException {
        try {
            if (ventaDTO.getTotal() < 0) {
                throw new NegociosException("El total de la venta no puede ser negativo");
            }

            Venta venta = Convertor.toVenta(ventaDTO, null); // Usuario se puede asignar después

            ventaDAO.save(venta);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar la venta", e);
        }
    }

    public VentaDTO obtenerVentaPorId(Long id) throws NegociosException {
        try {
            Venta venta = ventaDAO.findById(id);
            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            return Convertor.toVentaDTO(venta);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener la venta", e);
        }
    }

    public List<VentaDTO> obtenerTodos() throws NegociosException {
        try {
            List<Venta> ventas = ventaDAO.findAll();
            return Convertor.toVentaDTOList(ventas);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener todas las ventas", e);
        }
    }
}