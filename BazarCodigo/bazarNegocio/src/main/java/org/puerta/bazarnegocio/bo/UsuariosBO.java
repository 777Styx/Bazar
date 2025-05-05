/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.List;
import org.puerta.bazardependecias.dto.UsuarioDTO;
import org.puerta.bazardependecias.dto.UsuarioAuthDTO;
import org.puerta.bazarpersistencia.dominio.Usuario;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.Convertor;
import org.puerta.bazarpersistencia.dao.UsuariosDAO;

public class UsuariosBO {

    private UsuariosDAO usuariosDAO = new UsuariosDAO();

    /**
     * Método encargado de registrar un nuevo usuario en el sistema.
     *
     * @param usuarioDTO El objeto `UsuarioAuthDTO` que contiene los datos del
     * nuevo usuario a registrar.
     * @throws NegociosException Si ocurre un error al intentar registrar el
     * usuario, como un campo de nombre vacío o un error de persistencia al
     * guardar el usuario.
     */
    public void registrarUsuario(UsuarioAuthDTO usuarioDTO) throws NegociosException {
        try {
            // Verifica que el nombre del usuario no esté vacío
            if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().trim().isEmpty()) {
                throw new NegociosException("El nombre del usuario no puede estar vacío");
            }

            // Crea un nuevo objeto Usuario y establece sus propiedades
            Usuario usuario = new Usuario();
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setContrasena(usuarioDTO.getContrasena());

            // Guarda el usuario en la base de datos
            usuariosDAO.save(usuario);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al registrar el usuario", e);
        }
    }

    /**
     * Método encargado de autenticar a un usuario en el sistema utilizando su
     * nombre y contraseña.
     *
     * @param authDTO El objeto `UsuarioAuthDTO` que contiene el nombre de
     * usuario y la contraseña del usuario que intenta iniciar sesión.
     * @return `UsuarioDTO` El objeto `UsuarioDTO` que contiene los datos del
     * usuario autenticado.
     * @throws NegociosException Si ocurre un error durante el proceso de
     * autenticación, como un nombre de usuario o contraseña incorrectos o un
     * error de persistencia al consultar la base de datos.
     */
    public UsuarioDTO login(UsuarioAuthDTO authDTO) throws NegociosException {
        try {
            // Busca al usuario en la base de datos utilizando el nombre y la contraseña proporcionados
            Usuario usuario = usuariosDAO.findByNameAndPassword(authDTO.getNombre(), authDTO.getContrasena());

            // Si el usuario no se encuentra o la contraseña no coincide, lanza una excepción
            if (usuario == null || !usuario.getContrasena().equals(authDTO.getContrasena())) {
                throw new NegociosException("Nombre de usuario o contraseña incorrectos");
            }

            // Si la autenticación es exitosa, crea un objeto UsuarioDTO con la información del usuario autenticado
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNombre(usuario.getNombre());

            // Devuelve el UsuarioDTO con la información del usuario autenticado
            return usuarioDTO;
        } catch (PersistenciaException e) {
            // Si ocurre un error en la consulta a la base de datos, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al autenticar el usuario", e);
        }
    }

    /**
     * Método encargado de obtener los detalles de un usuario a partir de su ID.
     *
     * @param id El identificador único del usuario a obtener.
     * @return `UsuarioDTO` El objeto `UsuarioDTO` que contiene la información
     * del usuario obtenido.
     * @throws NegociosException Si ocurre un error al intentar obtener el
     * usuario, como si el usuario no existe o un error de persistencia al
     * consultar la base de datos.
     */
    public UsuarioDTO obtenerUsuarioPorId(Long id) throws NegociosException {
        try {
            // Busca el usuario en la base de datos utilizando el ID proporcionado
            Usuario usuario = usuariosDAO.findById(id);

            // Si el usuario no existe, lanza una excepción indicando que no se encontró
            if (usuario == null) {
                throw new NegociosException("Usuario no encontrado");
            }

            // Si el usuario es encontrado, crea un objeto UsuarioDTO con la información del usuario
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNombre(usuario.getNombre());

            // Devuelve el UsuarioDTO con la información del usuario encontrado
            return usuarioDTO;
        } catch (PersistenciaException e) {
            // Si ocurre un error al consultar la base de datos, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al obtener el usuario", e);
        }
    }

    /**
     * Método encargado de obtener todos los usuarios registrados en el sistema.
     *
     * @return `List<UsuarioDTO>` Una lista de objetos `UsuarioDTO` que
     * representan a todos los usuarios obtenidos de la base de datos.
     * @throws NegociosException Si ocurre un error al intentar obtener la lista
     * de usuarios, como un error de persistencia al consultar la base de datos.
     */
    public List<UsuarioDTO> obtenerTodosLosUsuarios() throws NegociosException {
        try {
            // Obtiene la lista de todos los usuarios desde la base de datos
            List<Usuario> usuarios = usuariosDAO.findAll();

            // Convierte la lista de usuarios a una lista de UsuarioDTO utilizando el convertidor
            return Convertor.toUsuarioDTOList(usuarios);
        } catch (PersistenciaException e) {
            // Si ocurre un error en la consulta a la base de datos, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al obtener la lista de usuarios", e);
        }
    }

}
