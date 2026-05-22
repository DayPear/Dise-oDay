/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adapters;

import Entitys.Pago;
import dtos.PagoDTO;
import java.time.LocalDateTime;

/**
 *
 * @author maria
 */
public class PagoAdapter {
    
    private PagoAdapter(){}
    
    public static PagoDTO convertirADTO(Pago entidad){
        if(entidad == null){
            return null;
        }
        
        return new PagoDTO(
                entidad.getIdTransaccion(), 
                entidad.getFechaOperacion(), 
                entidad.getImporte(), 
                entidad.getMetodoPago());
    }
    
    public static Pago convertirAEntidad(PagoDTO dto){
        if(dto == null){
            return null;
        }
        
        return new Pago(
                dto.getIdTransaccion(), 
                dto.getFechaOperacion(), 
                dto.getImporte(), 
                dto.getMetodoPago());
    }
}
