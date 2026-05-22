/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import dtos.AsientoEventoDTO;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author maria
 */
public interface IAsientoEventoBO {

    List<AsientoEventoDTO> consultarEstadosPorEvento(String idEvento) throws NegocioException;

    boolean reservarAsiento(String idAsiento) throws NegocioException;

    boolean liberarAsiento(String idAsiento) throws NegocioException;

    boolean venderAsiento(String idAsiento) throws NegocioException;
}
