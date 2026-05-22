/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.ReservacionDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author maria
 */
public interface IReservacionBO {
    
    boolean agregarReservacion(ReservacionDTO reservacion) throws NegocioException;
    
    List<ReservacionDTO> obtenerReservacionesUsuario(String idUsuario) throws NegocioException;
    
}
