/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import Entitys.ENUMS.ReservacionEstado;
import Entitys.Reservacion;
import entidadesmongo.PagoMongoEntidad;
import entidadesmongo.ReservacionMongoEntidad;
import entidadesresumenmongo.UsuarioResumenMongo;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author maria
 */
public class ReservacionPersistenciaAdapter {
    
    public static ReservacionMongoEntidad convertirAMongo(Reservacion dominio) throws PersistenciaException {
        if(dominio == null){
            return null;
        }
        
        ReservacionMongoEntidad mongo = new ReservacionMongoEntidad();
        
        mongo.setId(convertirStringAObjectId(dominio.getIdReservacion()));
        mongo.setTotal(dominio.getTotal());
        mongo.setBoleto(BoletoPersistenciaAdapter.convertirAMongo(dominio.getBoleto()));
        mongo.setPago(PagoPersistenciaAdapter.convertirAMongo(dominio.getPago()));
        
        if(dominio.getUsuario() != null){
            UsuarioResumenMongo u = new UsuarioResumenMongo(
                    convertirStringAObjectId(dominio.getUsuario().getIdUsuario()), 
                    dominio.getUsuario().getNombre(), 
                    dominio.getUsuario().getApellidoPaterno(), 
                    dominio.getUsuario().getCorreo());
            mongo.setUsuario(u);
        }
        
        if(dominio.getFechaHora() == null){
            mongo.setFechaRegistro(LocalDateTime.now());
        } else {
            mongo.setFechaRegistro(dominio.getFechaHora());
        }
        mongo.setEstado(dominio.getEstado().name());
        
        return mongo;
    }
    
    public static Reservacion convertirADominio(Document mongo) throws PersistenciaException {
        if(mongo == null){
            return null;
        }
        
        Reservacion dominio = new Reservacion();
        
        dominio.setIdReservacion(mongo.getObjectId("_id").toHexString());
        dominio.setTotal(mongo.getDouble("total"));
        
        dominio.setBoleto(BoletoPersistenciaAdapter.convertirADominio((Document) mongo.get("boleto")));
        
        Document pago = (Document) mongo.get("pago");
        if(pago != null){
            dominio.setPago(PagoPersistenciaAdapter.convertirADominio(pago));
        }
        
        Document usuario = (Document) mongo.get("usuario");
        if(usuario != null){
            dominio.setUsuario(UsuarioPersistenciaAdapter.convertirADominio(usuario));
        }
        
        dominio.setFechaHora(mongo.getDate("fechaRegistro")
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        dominio.setEstado(ReservacionEstado.valueOf(mongo.getString("estado")));
        
        return dominio;
    }
    
    public static List<Reservacion> convertirListaADominio(List<Document> lista) throws PersistenciaException {
        List<Reservacion> reservaciones = new ArrayList<>();
        
        if(lista == null){
            return new ArrayList<>();
        }
        
        for(Document mongo : lista){
            reservaciones.add(convertirADominio(mongo));
        }
        
        return reservaciones;
    }
    
    private static ObjectId convertirStringAObjectId(String id) throws PersistenciaException {
        if (id == null || id.isBlank()) {
            return null;
        }
        if (!ObjectId.isValid(id)) {
            throw new PersistenciaException(
                    "El id recibido no tiene formato válido de ObjectId."
            );
        }
        return new ObjectId(id);
    }
}
