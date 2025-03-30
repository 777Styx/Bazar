package resources;

import org.puerta.bazardependecias.dto.UsuarioDTO;

public class Sesion {
 
    private static UsuarioDTO usuarioActual;

    public static void setUsuarioActual(UsuarioDTO usuario) {
        Sesion.usuarioActual = usuario;
    }

    public static UsuarioDTO getUsuarioActual() {
        return Sesion.usuarioActual;
    }

    public static void cerrarSesion() {
        Sesion.usuarioActual = null;
    }
}
