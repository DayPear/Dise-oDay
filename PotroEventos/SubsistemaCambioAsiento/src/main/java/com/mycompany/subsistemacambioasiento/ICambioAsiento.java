/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.subsistemacambioasiento;

import dtos.AsientoDTO;
import dtos.BoletoDTO;
import java.util.List;

/**
 *
 * @author Dayanara Peralta G
 */
public interface ICambioAsiento {
    BoletoDTO consultarBoleto(String idBoleto);
    List<AsientoDTO> obtenerCatalogoAsientos();
    boolean confirmarCambioAsiento(String idBoleto, String idNuevoAsiento);
}
