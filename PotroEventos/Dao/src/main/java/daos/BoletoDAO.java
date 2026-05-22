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
            if (idReservacion == null) {
                throw new PersistenciaException("El ID de la reservación no puede ser nulo");
            }
            if (idAsientoNuevo == null) {
                throw new PersistenciaException("El ID del nuevo asiento no puede ser nulo");
            }
            
            UpdateResult resultado = coleccionReservaciones.updateOne(
                Filters.eq("_id", new ObjectId(idReservacion)),
                Updates.set("boleto.asiento", new ObjectId(idAsientoNuevo))
            );
            
            if (resultado.getMatchedCount() == 0) {
                throw new PersistenciaException("No se encontró el asiento");
            }
            
        }catch(MongoException e){
            throw new PersistenciaException("Error al actualizar el asiento del boleto: " + e.getMessage());
        }
    }
    
}
