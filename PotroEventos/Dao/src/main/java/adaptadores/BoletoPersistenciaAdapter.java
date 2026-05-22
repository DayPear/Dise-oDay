/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import Entitys.Asiento;
import Entitys.AsientoEvento;
import Entitys.Boleto;
import Entitys.ENUMS.EstadoBoleto;
import entidadesmongo.BoletoMongoEntidad;
import entidadesresumenmongo.AsientoEventoResumenMongo;
import entidadesresumenmongo.EventoResumenMongo;
import excepciones.PersistenciaException;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author maria
 */
public class BoletoPersistenciaAdapter {

    public static BoletoMongoEntidad convertirAMongo(Boleto dominio) throws PersistenciaException {
        if (dominio == null) {
            return null;
        }

        BoletoMongoEntidad mongo = new BoletoMongoEntidad();

        mongo.setId(dominio.getIdBoleto());
        mongo.setCodigoQR(dominio.getCodigoQR());
        mongo.setPrecio(dominio.getPrecio());

        if (dominio.getEstadoBoleto() != null) {
            mongo.setEstado(dominio.getEstadoBoleto().name());
        }

        mongo.setToken(dominio.getToken());

        if (dominio.getEvento() != null && dominio.getEvento().getIdEvento() != null) {
            EventoResumenMongo erm = new EventoResumenMongo();
            erm.setId(convertirStringAObjectId(dominio.getEvento().getIdEvento()));
            erm.setNombre(dominio.getEvento().getNombreEvento());
            erm.setFechaHora(dominio.getEvento().getFechaHora());
            mongo.setEvento(erm);
        }

        if (dominio.getAsiento() != null && dominio.getAsiento().getIdAsientoEvento() != null) {
            AsientoEventoResumenMongo aer = new AsientoEventoResumenMongo();
            aer.setIdAsientoEvento(convertirStringAObjectId(dominio.getAsiento().getIdAsientoEvento()));

            if (dominio.getAsiento().getAsiento() != null && dominio.getAsiento().getAsiento().getIdAsiento() != null) {
                aer.setAsiento(convertirStringAObjectId(dominio.getAsiento().getAsiento().getIdAsiento()));
                aer.setFila(dominio.getAsiento().getAsiento().getFila());
                aer.setNumero(dominio.getAsiento().getAsiento().getNumero());
                aer.setNombreSeccion(dominio.getAsiento().getAsiento().getSeccion().getNombre());
            }

            if (dominio.getEvento() != null && dominio.getEvento().getIdEvento() != null) {
                aer.setEvento(convertirStringAObjectId(dominio.getEvento().getIdEvento()));
            }
            mongo.setAsiento(aer);
        } else {
            mongo.setAsiento(null);
        }
        
        return mongo;
    }

    public static Boleto convertirADominio(Document mongo) throws PersistenciaException {
        if (mongo == null) {
            return null;
        }

        Boleto dominio = new Boleto();

        dominio.setCodigoQR(mongo.getString("codigoQR"));
        dominio.setEstadoBoleto(EstadoBoleto.valueOf(mongo.getString("estado")));
        dominio.setToken(mongo.getString("token"));

        Document evento = (Document) mongo.get("evento");
        if (evento != null) {
            dominio.setEvento(EventoPersistenciaAdapter.convertirADominio(evento));
        }

//        Document asiento = (Document) mongo.get("asiento");
//        if (asiento != null) {
//            Asiento a = new Asiento();
//            a.setFila(asiento.getString("fila"));
//            a.setNumero(asiento.getInteger("numero"));
//
//            AsientoEvento ae = new AsientoEvento();
//            ae.setIdAsientoEvento(asiento.getObjectId("idAsientoEvento").toHexString());
//            ae.setAsiento(a);
//
//            dominio.setAsiento(ae);
//        }

        return dominio;
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
