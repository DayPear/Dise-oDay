package interfaces;

import Entitys.Seccion;
import excepciones.PersistenciaException;
import java.util.List;

/**
 *
 * @author Kaleb
 */
public interface ISeccionDAO {

    List<Seccion> buscarPorEvento(String idEvento) throws PersistenciaException;
}
