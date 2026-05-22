package inicioSesion;

import dtos.UsuarioDTO;
/**
 * Clase controlador de inicio de sesion que gestiona el control interno de todo
 * lo que tiene que ver con la gestion de inicio de sesion
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
import dtos.LoginDTO;
import dtos.RegistroUsuarioDTO;
import excepciones.NegocioException;
import interfaces.IUsuarioBO;
import objetosNegocio.UsuarioBO;

public class ControlInicioSesion {

    private static ControlInicioSesion instance;

    private UsuarioDTO usuario;

    private final IUsuarioBO usuarioBO = UsuarioBO.getInstance();

    private ControlInicioSesion() {
    }

    public static ControlInicioSesion getIntance() {
        if (instance == null) {
            instance = new ControlInicioSesion();
        }
        return instance;
    }

    protected UsuarioDTO iniciarSesion(LoginDTO login) throws NegocioException {
        if (verificarUsuario(login)) {

        }
        return usuarioBO.obtenerUsuario(login);
    }

    protected boolean verificarUsuario(LoginDTO login) {
        if (login.getCorreo().isEmpty() || login.getContrasenia().isEmpty()) {
            return false;
        }
        return true;
    }

    protected void registrarSesion(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    protected void eliminarSesion() {
        this.usuario = null;
    }

    protected UsuarioDTO obtenerUsuarioActual() {
        return usuario;
    }

    protected UsuarioDTO registrarUsuario(RegistroUsuarioDTO usuario) throws NegocioException {
        return usuarioBO.guardarUsuario(usuario);
    }
}
