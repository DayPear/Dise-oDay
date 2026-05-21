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
import objetosNegocio.BoletoBO;
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
        this.boletoBO = BoletoBO.getInstance();
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
    
    public boolean cambiarAsiento(String idReservacion, String idAsientoNuevo) throws CambioAsientoException{
        String idAsientoActual = null;
        boolean ocupado = false;
        boolean liberado = false;
        try {
            System.out.println("[1] -> Entrando a ControlCambioAsiento.cambiarAsiento");
            System.out.println("   -> Param idReservacion: " + idReservacion);
            System.out.println("   -> Param idAsientoNuevo: " + idAsientoNuevo);

            BoletoDTO boleto = reservacionBO.obtenerBoletoPorReservacion(idReservacion);
            if (boleto == null) {
                System.out.println("[X] -> El boleto devuelto por reservacionBO es NULO");
                throw new CambioAsientoException("No se encontró el boleto para la reservación.");
            }
            System.out.println("[3] -> Boleto recuperado. ID: " + boleto.getCodigoQR());
            //idAsientoActual = boleto.getAsiento().getIdAsientoEvento();

            if (boleto.getAsiento() != null) {
                idAsientoActual = boleto.getAsiento().getIdAsientoEvento();
                System.out.println("[3.1] -> ID Asiento Anterior detectado: " + idAsientoActual);
            } else {
                System.out.println("[3.1] -> El boleto no tenía un asiento previo registrado (vino nulo). Se saltará la liberación.");
            }

            System.out.println("[3.2] -> ID Asiento Nuevo a registrar: " + idAsientoNuevo);

            System.out.println("[4] -> ID Asiento Actual a liberar: " + idAsientoActual);
            //asientoEventoBO.liberarAsiento(idAsientoActual);
            if (idAsientoActual != null && !idAsientoActual.trim().isEmpty()) {
                asientoEventoBO.liberarAsiento(idAsientoActual);
                System.out.println("[3.3] -> Asiento anterior liberado en BD.");
            }
            liberado = true;
            System.out.println("[5] -> Viejo asiento liberado en el BO");

            asientoEventoBO.ocuparAsiento(idAsientoNuevo);
            ocupado = true;
            System.out.println("[6] -> Nuevo asiento ocupado en el BO");

            //boletoBO.actualizarAsiento(idReservacion, idAsientoNuevo);
            System.out.println("[7] -> A punto de invocar a boletoBO.actualizarAsiento...");
            boletoBO.actualizarAsiento(idReservacion, idAsientoNuevo);
            System.out.println("[8] -> Se regresó de boletoBO.actualizarAsiento sin lanzar excepciones");
            return true;
        }catch(Exception e){
//            if (ocupado) {
//                try {
//                    asientoEventoBO.liberarAsiento(idAsientoNuevo);
//                } catch (Exception ex) {
//                }
//            }
//            if (liberado && idAsientoActual != null) {
//                try {
//                    asientoEventoBO.ocuparAsiento(idAsientoActual);
//                } catch (Exception ex) {
//                }
//            }
System.out.println("🚨 [CONTROL CATCH] Se disparó una excepción dentro de ControlCambioAsiento!");
        System.out.println("🚨 Tipo de error: " + e.getClass().getName());
        System.out.println("🚨 Mensaje exacto: " + e.getMessage());
        e.printStackTrace(); // Esto pintará las letras rojas con la línea exacta del fallo
            throw new CambioAsientoException("Error al cambiar el asiento: " + e.getMessage());
        }
    }
}