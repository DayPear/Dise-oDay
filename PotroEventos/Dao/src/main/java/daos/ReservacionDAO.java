/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import Entitys.Boleto;
import Entitys.Reservacion;
import adaptadores.BoletoPersistenciaAdapter;
import adaptadores.ReservacionPersistenciaAdapter;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.result.InsertOneResult;
import conexion.ConexionMongo;
import entidadesmongo.ReservacionMongoEntidad;
import excepciones.PersistenciaException;
import interfaces.IReservacionDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author maria
 */
public class ReservacionDAO implements IReservacionDAO {
    
    private MongoCollection<ReservacionMongoEntidad> coleccionReservaciones = ConexionMongo.obtenerColeccionReservaciones();
    private static ReservacionDAO instance;
    
    private ReservacionDAO() {
        // esto es para que no se repitan los qrs y su forma de buscarlos sea
        // más rápida.
        coleccionReservaciones
                .createIndex(
                        Indexes.ascending("boleto.codigoQR"), 
                        new IndexOptions().unique(true));
    }
    
    public static ReservacionDAO getInstance() {
        if (instance == null) {
            instance = new ReservacionDAO();
        }
        return instance;
    }
    
    @Override
    public boolean guardarReservacion(Reservacion reservacion) throws PersistenciaException {
        if (reservacion == null) {
            throw new PersistenciaException("La reservacion no puede ser nula.");
        }
        
        try {
            ReservacionMongoEntidad r = ReservacionPersistenciaAdapter.convertirAMongo(reservacion);
            
            InsertOneResult resultado = this.coleccionReservaciones.insertOne(r);
            
            if (resultado.getInsertedId() == null) {
                throw new PersistenciaException("Error al guardar.");
            }
            
            ObjectId idGenerado = resultado
                    .getInsertedId()
                    .asObjectId()
                    .getValue();
            
            r.setId(idGenerado);
            
            return resultado.wasAcknowledged() && resultado.getInsertedId() != null;
            
        } catch (MongoException e) {
            System.out.println("No fue posible guardar la reservación: " + e.getMessage());
            throw new PersistenciaException("No fue posible guardar la reservación: " + e.getMessage());
        }
        
    }
    
    @Override
    public List<Reservacion> obtenerReservacionesUsuario(String idUsuario) throws PersistenciaException {
        try {
            List<Document> reservaciones = coleccionReservaciones
                    .withDocumentClass(Document.class)
                    .aggregate(Arrays.asList(
                    Aggregates.match(Filters.eq("usuario._id", new ObjectId(idUsuario))),
                    Aggregates.lookup("usuarios", "usuario._id", "_id", "usuario"),
                    Aggregates.unwind("$usuario", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                    // hace el join para jalar el evento completo en el boleto
                    Aggregates.lookup("eventos", "boleto.evento._id", "_id", "evento_temp"),
                    Aggregates.unwind("$evento_temp", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                    Aggregates.addFields(new Field<>("boleto.evento", "$evento_temp")),
                    Aggregates.project(Projections.exclude("evento_temp")),
                    // hacer el join para la ubicación del evento
                    // hacer el join para que el evento tenga la ubicación, hazme el favor
                    Aggregates.lookup("ubicaciones", "boleto.evento.ubicacion._id", "_id", "ubicacion_temp"),
                    Aggregates.unwind("$ubicacion_temp", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                    Aggregates.addFields(new Field<>("boleto.evento.ubicacion", "$ubicacion_temp")),
                    Aggregates.project(Projections.exclude("ubicacion_temp"))
                    )).into(new ArrayList<>());
            return ReservacionPersistenciaAdapter.convertirListaADominio(reservaciones);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenciaException("No fue posible obtener las reservaciones");
        }
    }

    @Override
    public Boleto obtenerBoleto(String idReservacion) throws PersistenciaException {
        try {
            Document reservacion = coleccionReservaciones
                    .withDocumentClass(Document.class)
                    .aggregate(Arrays.asList(
                    Aggregates.match(Filters.eq("_id", new ObjectId(idReservacion))),
                    Aggregates.lookup("eventos", "boleto.evento._id", "_id", "boleto.evento"),
                    Aggregates.unwind("$boleto.evento", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                    Aggregates.lookup("asientos", "boleto.asiento._id", "_id", "boleto.asiento"),
                    Aggregates.unwind("$boleto.asiento", new UnwindOptions().preserveNullAndEmptyArrays(true))
                )).first();

            if (reservacion == null) {
                return null;
            }

            Document boleto = (Document) reservacion.get("boleto");
            
            return BoletoPersistenciaAdapter.convertirADominio(boleto);

        } catch (MongoException e) {
            throw new PersistenciaException("No fue posible obtener el boleto de la reservación.");
        }
    }
    
}
