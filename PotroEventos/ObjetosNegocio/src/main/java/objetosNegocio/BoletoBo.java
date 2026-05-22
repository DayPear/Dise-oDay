/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosNegocio;

import daos.BoletoDAO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IBoletoBO;
import interfaces.IBoletoDAO;

/**
 *
 * @author Dayanara Peralta G
 */
public class BoletoBo implements IBoletoBO{
    
    private static BoletoBo instancia;
    private final IBoletoDAO boletoDAO = BoletoDAO.getInstancia();
    
    public static BoletoBo getInstance(){
        if(instancia == null){
            instancia = new BoletoBo();
        }
        return instancia;
    }
    
    /**
     * Metodo que actualiza el asiento del boleto
     * @param idReservacion la reservación del boleto a cambiar el asiento
     * @param idAsientoNuevo el asiento nuevo
     * @throws NegocioException lanza excepción si no logra actualizar
     */
    @Override
    public void actualizarAsiento(String idReservacion, String idAsientoNuevo) throws NegocioException{
        if (idReservacion == null || idReservacion.trim().isEmpty()) {
            throw new NegocioException("ID de reservación no válido");
        }
        if (idAsientoNuevo == null || idAsientoNuevo.trim().isEmpty()) {
            throw new NegocioException("ID de asiento no válido");
        }
        try{
            boletoDAO.actualizarAsiento(idReservacion, idAsientoNuevo);
        } catch(PersistenciaException e){
            throw new NegocioException("Error al actualizar el asiento del boleto: " + e.getMessage());
        }
    }
}
