/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import Entitys.Boleto;
import Entitys.Reservacion;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author maria
 */
public interface IReservacionDAO {

    boolean guardarReservacion(Reservacion reservacion) throws PersistenciaException;

    List<Reservacion> obtenerReservacionesUsuario(String idUsuario) throws PersistenciaException;
    
    Boleto obtenerBoleto(String idReservacion) throws PersistenciaException;
}
