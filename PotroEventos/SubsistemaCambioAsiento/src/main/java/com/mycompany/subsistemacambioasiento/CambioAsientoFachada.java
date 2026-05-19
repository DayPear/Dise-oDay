/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.subsistemacambioasiento;

import Exception.CambioAsientoException;
import dtos.AsientoDTO;
import dtos.AsientoEventoDTO;
import dtos.BoletoDTO;
import dtos.ReservacionDTO;
import java.util.List;

/**
 *
 * @author Dayanara Peralta G
 */
public class CambioAsientoFachada implements ICambioAsiento{
    private ControlCambioAsiento controlCambio = ControlCambioAsiento.getInstance();

    @Override
    public BoletoDTO obtenerBoleto(String idReservacion) throws CambioAsientoException{
        return controlCambio.obtenerBoleto(idReservacion);
    }

    @Override
    public boolean verificarDisponibilidad(String idAsiento) throws CambioAsientoException{
        return controlCambio.validarDisponibilidad(idAsiento);
    }

    @Override
    public boolean cambiarAsiento(String idReservacion, String idAsientoNuevo) throws CambioAsientoException {
        try {
            controlCambio.cambiarAsiento(idReservacion, idAsientoNuevo);
            return true;
        } catch (CambioAsientoException e) {
            return false;
        }
    }

}
