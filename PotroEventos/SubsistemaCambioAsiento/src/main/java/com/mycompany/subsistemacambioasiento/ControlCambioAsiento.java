/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.subsistemacambioasiento;

import Exception.CambioAsientoException;
import dtos.BoletoDTO;
import dtos.ReservacionDTO;
import interfaces.IAsientoEventoBO;
import interfaces.IBoletoBO;
import interfaces.IReservacionBO;
import objetosNegocio.AsientoEventoBO;
import objetosNegocio.BoletoBo;
import objetosNegocio.ReservacionBO;

/**
 *
 * @author Dayanara Peralta G
 */
public class ControlCambioAsiento {
    private IReservacionBO reservacionBO;
    private IAsientoEventoBO asientoEventoBO;
    private IBoletoBO boletoBO;
    
    private static ControlCambioAsiento instance;

    public ControlCambioAsiento() {
        this.reservacionBO = ReservacionBO.getInstance();
        this.asientoEventoBO = AsientoEventoBO.getInstance();
        this.boletoBO = BoletoBo.getInstance();
    }
    
    public static ControlCambioAsiento getInstance(){
        if(instance == null){
            instance = new ControlCambioAsiento();
        }
        return instance;
    }
    
    public BoletoDTO obtenerBoleto(String idReservacion) throws CambioAsientoException{
        try{
            return reservacionBO.obtenerBoletoPorReservacion(idReservacion);
        }catch(Exception e ){
            throw new CambioAsientoException("Error al obtener el boleto: " + e.getMessage());
        }
    }
    
    public boolean validarDisponibilidad(String idAsiento) throws CambioAsientoException{
        try{
            return asientoEventoBO.validarDisponibilidadAsiento(idAsiento);
        }catch(Exception e){
            throw new CambioAsientoException("Error al validar la disponibilidad del asiento: " + e.getMessage());
        }
    }
    
    public void cambiarAsiento(String idReservacion, String idAsientoNuevo) throws CambioAsientoException{
        try{
            if(!validarDisponibilidad(idAsientoNuevo)){
                throw new CambioAsientoException("El asiento no esta disponible");
            }
            
            BoletoDTO boleto = reservacionBO.obtenerBoletoPorReservacion(idReservacion);
            
            String idAsientoActual = boleto.getAsiento().getIdAsientoEvento();
            
            asientoEventoBO.liberarAsiento(idAsientoActual);
            
            asientoEventoBO.ocuparAsiento(idAsientoNuevo);
            
            boletoBO.actualizarAsiento(idReservacion, idAsientoNuevo);
        }catch(Exception e){
            throw new CambioAsientoException("Error al cambiar el asiento: " + e.getMessage());
        }
    }
}