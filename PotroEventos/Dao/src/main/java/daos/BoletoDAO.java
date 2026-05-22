/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionMongo;
import entidadesmongo.BoletoMongoEntidad;
import excepciones.PersistenciaException;
import interfaces.IBoletoDAO;
import java.util.Arrays;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Dayanara Peralta G
 */
public class BoletoDAO implements IBoletoDAO {

    private static BoletoDAO instancia;
    private final MongoCollection<BoletoMongoEntidad> coleccionBoletos;

    public BoletoDAO() {
        this.coleccionBoletos = ConexionMongo.obtenerColeccionBoletos();
    }

    public static BoletoDAO getInstancia() {
        if (instancia == null) {
            instancia = new BoletoDAO();
        }
        return instancia;
    }

    /**
     * Metodo que actualiza en asiento de una reservacion
     *
     * @param idReservacion la reservacion a la que se desea actualizar el
     * asiento
     * @param idAsientoNuevo el asiento nuevo a actualizar en la reservacion
     * @throws PersistenciaException
     */
    @Override
    public void actualizarAsiento(String idReservacion, String idAsientoNuevo) throws PersistenciaException {
        try {
            if (idReservacion == null) {
                throw new PersistenciaException("El ID de la reservación no puede ser nulo");
            }
            if (idAsientoNuevo == null) {
                throw new PersistenciaException("El ID del nuevo asiento no puede ser nulo");
            }

            MongoCollection<Document> coleccionAsientosEvento = ConexionMongo
                    .obtenerBaseDatos()
                    .getCollection("asientoEventos");

            Document asientoEventoDoc = coleccionAsientosEvento
                    .aggregate(Arrays.asList(
                            Aggregates.match(Filters.eq("_id", new ObjectId(idAsientoNuevo))),
                            Aggregates.lookup("asientos", "asiento", "_id", "asiento_doc"),
                            Aggregates.unwind("$asiento_doc"),
                            Aggregates.lookup("ubicaciones", "asiento_doc.ubicacion", "_id", "ubicacion_temp"),
                            Aggregates.unwind("$ubicacion_temp"),
                            Aggregates.addFields(
                                    new Field<>("asiento_doc.seccion",
                                            new Document("$arrayElemAt", Arrays.asList(
                                                    new Document("$filter",
                                                            new Document("input", "$ubicacion_temp.secciones")
                                                                    .append("as", "s")
                                                                    .append("cond", new Document("$eq",
                                                                            Arrays.asList("$$s._id", "$asiento_doc.seccion")))
                                                    ),
                                                    0
                                            ))
                                    )
                            )
                    ))
                    .first();

            if (asientoEventoDoc == null) {
                throw new PersistenciaException("No se encontró el nuevo asiento");
            }

            Document asientoDoc = (Document) asientoEventoDoc.get("asiento_doc");
            Document seccionDoc = (Document) asientoDoc.get("seccion");

            Document nuevoAsiento = new Document()
                    .append("idAsientoEvento", new ObjectId(idAsientoNuevo))
                    .append("idComoTexto", idAsientoNuevo)
                    .append("fila", asientoDoc.getString("fila"))
                    .append("numero", asientoDoc.getInteger("numero"))
                    .append("nombreSeccion", seccionDoc != null ? seccionDoc.getString("nombre") : "");

            MongoCollection<Document> coleccionReservaciones = ConexionMongo
                    .obtenerBaseDatos()
                    .getCollection("reservaciones");

            UpdateResult resultado = coleccionReservaciones.updateOne(
                    Filters.eq("_id", new ObjectId(idReservacion)),
                    Updates.set("boleto.asiento", nuevoAsiento)
            );

            if (resultado.getMatchedCount() == 0) {
                throw new PersistenciaException("No se encontró la reservación");
            }
            if (resultado.getModifiedCount() == 0) {
                throw new PersistenciaException("No se modificó ningún documento");
            }

        } catch (MongoException e) {
            throw new PersistenciaException("Error al actualizar el asiento: " + e.getMessage());
        }
    }
}
