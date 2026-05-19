/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
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
public interface ICambioAsiento {
    BoletoDTO obtenerBoleto(String idReservacion) throws CambioAsientoException;
    boolean verificarDisponibilidad(String idAsiento) throws CambioAsientoException;
    boolean cambiarAsiento(String idReservacion, String idAsientoNuevo) throws CambioAsientoException;
}
