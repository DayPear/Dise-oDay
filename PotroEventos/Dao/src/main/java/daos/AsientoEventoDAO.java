package daos;

import Entitys.AsientoEvento;
import Entitys.ENUMS.EstadoAsiento;
import adaptadores.AsientoEventoPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionMongo;
import entidadesmongo.AsientoEventoMongoEntidad;
import excepciones.PersistenciaException;
import interfaces.IAsientoEventoDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * DAO mock para la gestión de asientos por evento.
 *
 * Esta implementación utiliza una lista en memoria a nivel de clase para
 * facilitar pruebas unitarias y simulaciones sin necesidad de base de datos
 * real.
 *
 * Permite consultar los asientos de un evento y reservar un asiento únicamente
 * si aún se encuentra disponible.
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class AsientoEventoDAO implements IAsientoEventoDAO {

    private static AsientoEventoDAO instancia;

    /**
     * Lista mock a nivel de clase para persistencia en memoria. Esto permite
     * que las pruebas mantengan estado entre llamadas.
     */
    private final MongoCollection<AsientoEventoMongoEntidad> coleccionAsientosEventos;

    private AsientoEventoDAO() {
        this.coleccionAsientosEventos = ConexionMongo.obtenerColeccionAsientosEvento();
    }

    public static AsientoEventoDAO getInstancia() {
        if (instancia == null) {
            instancia = new AsientoEventoDAO();
        }
        return instancia;
    }

    @Override
    public List<AsientoEvento> buscarPorEvento(String idEvento) throws PersistenciaException {
        try {
            List<Document> asientos = coleccionAsientosEventos
                    .withDocumentClass(Document.class)
                    .aggregate(Arrays.asList(
                            Aggregates.match(Filters.eq("evento", new ObjectId(idEvento))),
                            // hace el join con el asiento
                            Aggregates.lookup("asientos", "asiento", "_id", "asiento_doc"),
                            Aggregates.unwind("$asiento_doc", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            // hace el join para jalar la ubicación completa del asiento
                            Aggregates.lookup("ubicaciones", "asiento_doc.ubicacion", "_id", "ubicacion_temp"),
                            Aggregates.unwind("$ubicacion_temp", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            // hace que las referencias en asiento ahora sean objetos completos
                            Aggregates.addFields(
                                    new Field<>("asiento_doc.ubicacion", "$ubicacion_temp"),
                                    new Field<>("asiento_doc.seccion",
                                            new Document("$arrayElemAt", Arrays.asList(
                                                    new Document("$filter",
                                                            new Document("input", "$ubicacion_temp.secciones")
                                                                    .append("as", "s")
                                                                    .append("cond", new Document("$eq", Arrays.asList("$$s._id", "$asiento_doc.seccion")))
                                                    ),
                                                    0
                                            ))
                                    )
                            ),
                            // desecha el objeto temporal
                            Aggregates.project(Projections.exclude("ubicacion_temp")),
                            // ahora hace el join con el evento
                            Aggregates.lookup("eventos", "evento", "_id", "evento_doc"),
                            Aggregates.unwind("$evento_doc", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            // hace el join para pasarle la ubicación al evento
                            Aggregates.lookup("ubicaciones", "evento_doc.ubicacion._id", "_id", "ubicacion_temp2"),
                            Aggregates.unwind("$ubicacion_temp2", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            Aggregates.addFields(new Field<>("evento_doc.ubicacion", "$ubicacion_temp2")),
                            Aggregates.project(Projections.exclude("ubicacion_temp2"))
                    ))
                    .into(new ArrayList<>());
            return AsientoEventoPersistenciaAdapter.convertirListaADominio(asientos);
        } catch (MongoException me) {
            throw new PersistenciaException("No fue posible consultar los asientos del evento.");
        }
    }

    @Override
    public boolean reservarAsiento(String idAsiento) throws PersistenciaException {
        try {
            AsientoEventoMongoEntidad asiento = this.coleccionAsientosEventos
                    .find(eq("_id", new ObjectId(idAsiento)))
                    .first();
            
            if (asiento == null) {
                throw new PersistenciaException("AsientoEvento no encontrado");
            }

            asiento.setEstado(EstadoAsiento.RESERVADO.name());

            UpdateResult resultado = this.coleccionAsientosEventos
                    .replaceOne(eq("_id", new ObjectId(asiento.getIdComoTexto())), asiento);
            
            if (resultado.getMatchedCount() == 0) {
                throw new PersistenciaException("No se encontró el asiento");
            }
            return true;

        } catch (MongoException e) {
            throw new PersistenciaException("No fue posible reservar el asiento");
        }
    }

    @Override
    public boolean liberarAsiento(String idAsiento) throws PersistenciaException {
        try {
            AsientoEventoMongoEntidad asiento = this.coleccionAsientosEventos
                    .find(eq("_id", new ObjectId(idAsiento)))
                    .first();
            
            if (asiento == null) {
                throw new PersistenciaException("AsientoEvento no encontrado");
            }

            asiento.setEstado(EstadoAsiento.DISPONIBLE.name());

            UpdateResult resultado = this.coleccionAsientosEventos.
                    replaceOne(eq("_id", new ObjectId(asiento.getIdComoTexto())), asiento);
            
            if (resultado.getMatchedCount() == 0) {
                throw new PersistenciaException("No se encontró el asiento");
            }
            return true;

        } catch (MongoException e) {
            throw new PersistenciaException("No fue posible liberar el asiento");
        }
    }

    @Override
    public boolean venderAsiento(String idAsiento) throws PersistenciaException {
        try {
            AsientoEventoMongoEntidad asiento = this.coleccionAsientosEventos
                    .find(eq("_id", new ObjectId(idAsiento)))
                    .first();
            
            if (asiento == null) {
                throw new PersistenciaException("AsientoEvento no encontrado");
            }

            asiento.setEstado(EstadoAsiento.VENDIDO.name());

            UpdateResult resultado = this.coleccionAsientosEventos.
                    replaceOne(eq("_id", new ObjectId(asiento.getIdComoTexto())), asiento);
            
            if (resultado.getMatchedCount() == 0) {
                throw new PersistenciaException("No se encontró el asiento");
            }
            return true;

        } catch (MongoException e) {
            throw new PersistenciaException("No fue posible vender el asiento");
        }
    }

    @Override
    public AsientoEvento consultarPorId(String idAsiento) throws PersistenciaException {
        try {
            Document asiento = coleccionAsientosEventos
                    .withDocumentClass(Document.class)
                    .aggregate(Arrays.asList(
                            Aggregates.match(Filters.eq("_id", new ObjectId(idAsiento))),
                            // hace el join con el asiento
                            Aggregates.lookup("asientos", "asiento", "_id", "asiento_doc"),
                            Aggregates.unwind("$asiento_doc", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            // ahora hace el join para obtener la ubicación
                            Aggregates.lookup("ubicaciones", "asiento_doc.ubicacion", "_id", "ubicacion_temp"),
                            Aggregates.unwind("$ubicacion_temp", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            // transforma las referencias en objetos completos
                            Aggregates.addFields(
                                    new Field<>("asiento_doc.ubicacion", "$ubicacion_temp"),
                                    new Field<>("asiento_doc.seccion",
                                            new Document("$arrayElemAt", Arrays.asList(
                                                    new Document("$filter",
                                                            new Document("input", "$ubicacion_temp.secciones")
                                                                    .append("as", "s")
                                                                    .append("cond", new Document("$eq", Arrays.asList("$$s._id", "$asiento_doc.seccion")))
                                                    ),
                                                    0
                                            ))
                                    )
                            ),
                            Aggregates.project(Projections.exclude("ubicacion_temp")),
                            // hace join con el evento
                            Aggregates.lookup("eventos", "evento", "_id", "evento_doc"),
                            Aggregates.unwind("$evento_doc", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            // hace el join para pasarle la ubicación pero al evento
                            Aggregates.lookup("ubicaciones", "evento_doc.ubicacion._id", "_id", "ubicacion_temp2"),
                            Aggregates.unwind("$ubicacion_temp2", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            Aggregates.addFields(new Field<>("evento_doc.ubicacion", "$ubicacion_temp2")),
                            Aggregates.project(Projections.exclude("ubicacion_temp2"))  
                    ))
                    .first();

            return AsientoEventoPersistenciaAdapter.convertirADominio(asiento);
        } catch (MongoException me) {
            throw new PersistenciaException("No fue posible obtener el asiento del evento.");
        }
    }

}
