package interfaces;

import Entitys.Seccion;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author Kaleb
 */
public interface ISeccionDAO {
    
    Seccion buscarPorId(String idSeccion) throws PersistenciaException;

    List<Seccion> buscarPorEvento(String idEvento) throws PersistenciaException;
}
