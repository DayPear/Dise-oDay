/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestionUsuarios;

import dtos.ReservacionDTO;
import dtos.UsuarioDTO;
import excepciones.GestionUsuarioException;
import java.util.List;

/**
 * Interfaz que sirve como puente entre la fachada y la capa de presentación.
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public interface IGestionUsuariosFachada {
    
    public boolean asociarUsuario(UsuarioDTO usuario) throws GestionUsuarioException;
    
    public UsuarioDTO obtenerUsuarioActivo() throws GestionUsuarioException ;
    
    public List<ReservacionDTO> obtenerReservacionesUsuario(String idUsuario) throws GestionUsuarioException;
}
