/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.subsistemacambioasiento;

import dtos.AsientoDTO;
import dtos.BoletoDTO;
import java.util.List;

/**
 *
 * @author Dayanara Peralta G
 */
public class CambioAsientoFachada implements ICambioAsiento{

    @Override
    public BoletoDTO consultarBoleto(String idBoleto) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<AsientoDTO> obtenerCatalogoAsientos() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean confirmarCambioAsiento(String idBoleto, String idNuevoAsiento) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
