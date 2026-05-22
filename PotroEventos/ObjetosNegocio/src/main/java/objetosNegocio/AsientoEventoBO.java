/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosNegocio;

import Entitys.AsientoEvento;
import adapters.AsientoEventoAdapter;
import daos.AsientoEventoDAO;
import dtos.AsientoEventoDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IAsientoEventoBO;
import interfaces.IAsientoEventoDAO;
import java.util.List;

/**
 *
 * @author maria
 */
public class AsientoEventoBO implements IAsientoEventoBO {

    private static AsientoEventoBO instancia;

    private IAsientoEventoDAO asientoEventoDAO = AsientoEventoDAO.getInstancia();

    private AsientoEventoBO() {
    }

    public static AsientoEventoBO getInstance() {
        if (instancia == null) {
            instancia = new AsientoEventoBO();
        }
        return instancia;
    }

    @Override
    public List<AsientoEventoDTO> consultarEstadosPorEvento(String idEvento) throws NegocioException {
        // Validaciones de negocio previas
        if (idEvento == null) {
            throw new NegocioException("ID de evento no válido.");
        }

        try {
            // 1. Obtener entidades del DAO
            List<AsientoEvento> entidades = asientoEventoDAO.buscarPorEvento(idEvento);

            // 2. Convertir a DTOs usando el Adapter (Limpio y directo)
            return AsientoEventoAdapter.listaEntidadADTO(entidades);

        } catch (PersistenciaException e) {
            // Log de error y relanzamiento como NegocioException
            System.err.println("Error en AsientoEventoBO: " + e.getMessage());
            throw new NegocioException("No se pudo cargar la ocupación del evento.");
        }
    }

    @Override
    public boolean reservarAsiento(String idAsiento) throws NegocioException {
        if (idAsiento == null) {
            throw new NegocioException("ID de asientoEvento no válido.");
        }

        try {
            return asientoEventoDAO.reservarAsiento(idAsiento);
        } catch (PersistenciaException e) {
            throw new NegocioException("No fue posible reservar el asiento");
        }
    }

    @Override
    public boolean liberarAsiento(String idAsiento) throws NegocioException {
        if (idAsiento == null) {
            throw new NegocioException("ID de asientoEvento no válido.");
        }

        try {
            return asientoEventoDAO.liberarAsiento(idAsiento);
        } catch (PersistenciaException e) {
            throw new NegocioException("No fue posible liberar el asiento");
        }
    }

    @Override
    public boolean venderAsiento(String idAsiento) throws NegocioException {
        if (idAsiento == null) {
            throw new NegocioException("ID de asientoEvento no válido.");
        }

        try {
            return asientoEventoDAO.venderAsiento(idAsiento);
        } catch (PersistenciaException e) {
            throw new NegocioException("No fue posible liberar el asiento");
        }
    }
}
