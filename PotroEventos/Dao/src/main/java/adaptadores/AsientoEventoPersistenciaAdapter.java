/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import Entitys.AsientoEvento;
import Entitys.ENUMS.EstadoAsiento;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author maria
 */
public class AsientoEventoPersistenciaAdapter {

    public static AsientoEvento convertirADominio(Document mongo) throws PersistenciaException {
        if (mongo == null) {
            return null;
        }
        AsientoEvento dominio = new AsientoEvento();

        dominio.setIdAsientoEvento(mongo.getObjectId("_id").toHexString());
        Double precio = ((Number) mongo.get("precio")).doubleValue();
        dominio.setPrecio(precio);
        dominio.setEstadoAsiento(EstadoAsiento.valueOf(mongo.getString("estado")));

        Document asientoDoc = (Document) mongo.get("asiento_doc");
        if (asientoDoc != null) {
            dominio.setAsiento(AsientoPersistenciaAdapter.convertirADominio(asientoDoc));
        }

        Document eventoDoc = (Document) mongo.get("evento_doc");
        if (eventoDoc != null) {
            dominio.setEvento(EventoPersistenciaAdapter.convertirADominio(eventoDoc));
        }

        return dominio;
    }

    public static List<AsientoEvento> convertirListaADominio(List<Document> lista) throws PersistenciaException {
        List<AsientoEvento> asientos = new ArrayList<>();

        if (lista == null) {
            return asientos;
        }

        for (Document mongo : lista) {
            asientos.add(convertirADominio(mongo));
        }

        return asientos;
    }
    
}
