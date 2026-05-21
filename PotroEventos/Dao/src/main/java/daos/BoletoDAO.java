/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import conexion.ConexionMongo;
import entidadesmongo.ReservacionMongoEntidad;
import excepciones.PersistenciaException;
import interfaces.IBoletoDAO;
import org.bson.types.ObjectId;

/**
 *
 * @author Dayanara Peralta G
 */
public class BoletoDAO implements IBoletoDAO{
    private static BoletoDAO instancia;
    private final MongoCollection<ReservacionMongoEntidad> coleccionReservaciones;
    
    public BoletoDAO() {
        this.coleccionReservaciones = ConexionMongo.obtenerColeccionReservaciones();
    }
    
    public static BoletoDAO getInstancia(){
        if(instancia == null){
            instancia = new BoletoDAO();
        }
        return instancia;
    }

    @Override
    public void actualizarAsiento(String idReservacion, String idAsientoNuevo) throws PersistenciaException {
        try{
            if (idReservacion == null || idAsientoNuevo == null) {
                throw new PersistenciaException("Los IDs no puede ser nulo");
            }
            
            var coleccionAsientos = conexion.ConexionMongo.obtenerColeccionAsientosEvento();

            org.bson.Document nuevoAsientoDoc = (org.bson.Document) coleccionAsientos.find(
                    com.mongodb.client.model.Filters.eq("_id", new org.bson.types.ObjectId(idAsientoNuevo)),
                     org.bson.Document.class).first();
            
            if (nuevoAsientoDoc == null) {
                throw new PersistenciaException("ENo se encontró el nuevo asiento en la BD para incrustarlo.");
            }
            
            UpdateResult resultado = coleccionReservaciones.updateOne(
                Filters.eq("_id", new ObjectId(idReservacion)),
                Updates.set("boleto.asiento", new ObjectId(idAsientoNuevo))
            );
            
            System.out.println("DAO - Coincidencias encontradas (Matched): " + resultado.getMatchedCount());
            System.out.println("DAO - Documentos modificados (Modified): " + resultado.getModifiedCount());
            
//            if (resultado.getMatchedCount() == 0) {
//                throw new PersistenciaException("No se encontró el asiento");
//            }
//            if (resultado.getModifiedCount() == 0) {
//                System.out.println("ADVERTENCIA: La reservación existe pero no se modificó. Puede que el asiento ya sea el mismo.");
//            }
            
        }catch(MongoException e){
            System.err.println("Error interno de Mongo: " + e.getMessage());
            throw new PersistenciaException("Error al actualizar el asiento del boleto: " + e.getMessage());
        }
    }
    
}
