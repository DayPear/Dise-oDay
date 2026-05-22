/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import Entitys.AsientoEvento;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author Kaleb
 */
public interface IAsientoEventoDAO {
    
    AsientoEvento consultarPorId(String idAsiento) throws PersistenciaException;

    List<AsientoEvento> buscarPorEvento(String idEvento) throws PersistenciaException;

    boolean reservarAsiento(String idAsiento) throws PersistenciaException;

    boolean liberarAsiento(String idAsiento) throws PersistenciaException;
    
    boolean venderAsiento(String idAsiento) throws PersistenciaException;
}
