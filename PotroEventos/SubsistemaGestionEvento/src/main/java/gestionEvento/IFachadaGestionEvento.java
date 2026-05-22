/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestionEvento;

import dtos.CategoriaDTO;
import dtos.EventoDTO;
import excepciones.GestionEventoException;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author maria
 */
public interface IFachadaGestionEvento {

    EventoDTO consultarEvento(String idEvento) throws GestionEventoException;

    List<EventoDTO> consultarEventosPorCategoria(CategoriaDTO categoria) throws GestionEventoException;

    List<CategoriaDTO> consultarCategorias() throws GestionEventoException;
}
