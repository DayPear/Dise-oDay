package daos;

import Entitys.Seccion;
import adaptadores.SeccionPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import conexion.ConexionMongo;
import entidadesmongo.SeccionMongoEntidad;
import excepciones.PersistenciaException;
import interfaces.ISeccionDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Kaleb
 */
public class SeccionDAO implements ISeccionDAO {

    private MongoCollection<SeccionMongoEntidad> coleccionSecciones;

    private static SeccionDAO instancia;

    public SeccionDAO() {
        this.coleccionSecciones = ConexionMongo.obtenerColeccionSecciones();
    }

    public static SeccionDAO getInstance() {
        if (instancia == null) {
            instancia = new SeccionDAO();
        }
        return instancia;
    }

    @Override
    public List<Seccion> buscarPorEvento(String idEvento) throws PersistenciaException {
        try {
            List<SeccionMongoEntidad> secciones = coleccionSecciones.find(eq("idEvento", new ObjectId(idEvento))).into(new ArrayList<>());
            return SeccionPersistenciaAdapter.convertirListaADominio(secciones);
        } catch (MongoException e) {
            throw new PersistenciaException("No fue posible obtener las seciones");
        }
    }

}
