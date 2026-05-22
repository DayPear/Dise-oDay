/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.PersistenciaException;

/**
 *
 * @author Dayanara Peralta G
 */
public interface IBoletoDAO {
    public void actualizarAsiento(String idReservacion, String idAsientoNuevo) throws PersistenciaException;
}
