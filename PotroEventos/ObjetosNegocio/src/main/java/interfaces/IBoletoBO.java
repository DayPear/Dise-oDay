/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import excepciones.NegocioException;

/**
 *
 * @author Dayanara Peralta G
 */
public interface IBoletoBO {

    /**
     * Metodo que actualiza el asiento del boleto
     * @param idReservacion la reservación del boleto a cambiar el asiento
     * @param idAsientoNuevo el asiento nuevo
     * @throws NegocioException lanza excepción si no logra actualizar
     */
    public void actualizarAsiento(String idReservacion, String idAsientoNuevo) throws NegocioException;
}
