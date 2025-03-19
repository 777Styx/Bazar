/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.List;
import org.puerta.bazardependecias.dto.UsuarioDTO;
import org.puerta.bazardependecias.dto.UsuarioAuthDTO;
import org.puerta.bazardependecias.dominio.Usuario;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazardependecias.utils.Convertor;
import org.puerta.bazarpersistencia.dao.UsuariosDAO;

public class UsuariosBO {

    private UsuariosDAO usuariosDAO = new UsuariosDAO();

    // Método para registrar un usuario
    public void registrarUsuario(UsuarioAuthDTO usuarioDTO) throws NegociosException {
        try {
            // Validar que el nombre no esté vacío
            if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().trim().isEmpty()) {
                throw new NegociosException("El nombre del usuario no puede estar vacío");
            }

            // Convertir DTO a entidad
            Usuario usuario = new Usuario();
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setContrasena(usuarioDTO.getContrasena()); // Aquí deberías encriptar la contraseña

            // Guardar el usuario
            usuariosDAO.save(usuario);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el usuario", e);
        }
    }

    // Método para autenticar un usuario (login)
    public UsuarioDTO login(UsuarioAuthDTO authDTO) throws NegociosException {
        try {
            // Buscar el usuario por nombre
            Usuario usuario = usuariosDAO.findByNameAndPassword(authDTO.getNombre(), authDTO.getContrasena());

            // Validar la contraseña
            if (usuario == null || !usuario.getContrasena().equals(authDTO.getContrasena())) {
                throw new NegociosException("Nombre de usuario o contraseña incorrectos");
            }

            // Convertir entidad a DTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNombre(usuario.getNombre());

            return usuarioDTO;
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al autenticar el usuario", e);
        }
    }

    // Método para obtener un usuario por ID
    public UsuarioDTO obtenerUsuarioPorId(Long id) throws NegociosException {
        try {
            Usuario usuario = usuariosDAO.findById(id);
            if (usuario == null) {
                throw new NegociosException("Usuario no encontrado");
            }

            // Convertir entidad a DTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNombre(usuario.getNombre());

            return usuarioDTO;
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el usuario", e);
        }
    }
}