/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosNegocio;

import Entitys.Asiento;
import adapters.AsientoAdapter;
import daos.AsientoDAO;
import dtos.AsientoDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IAsientoBO;
import java.util.List;

/**
 *
 * @author maria
 */
public class AsientoBO implements IAsientoBO {

    private static AsientoBO instancia;

    private AsientoDAO asientoDAO = AsientoDAO.getInstance();

    private AsientoBO() {
    }

    public static AsientoBO getInstance() {
        if (instancia == null) {
            instancia = new AsientoBO();
        }
        return instancia;
    }

    @Override
    public List<AsientoDTO> consultarAsientos() throws NegocioException {
        try {
            return AsientoAdapter.entidadesADTO(asientoDAO.consultarAsientos());
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

}
