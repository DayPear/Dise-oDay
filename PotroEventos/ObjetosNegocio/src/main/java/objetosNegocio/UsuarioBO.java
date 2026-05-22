/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosNegocio;

import adapters.UsuarioAdapter;
import dtos.LoginDTO;
import dtos.UsuarioDTO;
import interfaces.IUsuarioBO;
import daos.UsuarioDAO;
import dtos.RegistroUsuarioDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IUsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author aaron
 */
public class UsuarioBO implements IUsuarioBO {

    private static UsuarioBO instance;

    private IUsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    private UsuarioBO() {

    }

    public static UsuarioBO getInstance() {
        if (instance == null) {
            instance = new UsuarioBO();
        }
        return instance;
    }

    @Override
    public UsuarioDTO obtenerUsuario(LoginDTO sesion) throws NegocioException {
        try {

            UsuarioDTO usuarioBaseDatos = UsuarioAdapter.entidadADTO(usuarioDAO.obtenerUsuario(UsuarioAdapter.dtoLoginAEntidad(sesion)));

            if(usuarioBaseDatos == null){
                return null;
            }
            
            return usuarioBaseDatos;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public UsuarioDTO guardarUsuario(RegistroUsuarioDTO dto) throws NegocioException {
        try {
            return UsuarioAdapter.entidadADTO(usuarioDAO.guardarUsuario(UsuarioAdapter.dtoRegistroAEntidad(dto)));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorId(String idUsuario) throws NegocioException {
        try {
            return UsuarioAdapter.entidadADTO(usuarioDAO.obtenerPorId(idUsuario));
        } catch (PersistenciaException pe) {
            throw new NegocioException(pe.getMessage());
        }
    }

}
