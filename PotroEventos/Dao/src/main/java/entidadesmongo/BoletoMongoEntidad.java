/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidadesmongo;

import entidadesresumenmongo.AsientoEventoResumenMongo;
import entidadesresumenmongo.EventoResumenMongo;

/**
 *
 * @author maria
 */
public class BoletoMongoEntidad {
    
    private String id;
    private String estado;
    private EventoResumenMongo evento;
    private AsientoEventoResumenMongo asiento;
    private String token;
    private double precio;
    private String codigoQR;

    public BoletoMongoEntidad() {
    }

    public BoletoMongoEntidad(String id, String estado, EventoResumenMongo evento, AsientoEventoResumenMongo asiento, String token, double precio, String codigoQR) {
        this.id = id;
        this.estado = estado;
        this.evento = evento;
        this.asiento = asiento;
        this.token = token;
        this.precio = precio;
        this.codigoQR = codigoQR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EventoResumenMongo getEvento() {
        return evento;
    }

    public void setEvento(EventoResumenMongo evento) {
        this.evento = evento;
    }

    public AsientoEventoResumenMongo getAsiento() {
        return asiento;
    }

    public void setAsiento(AsientoEventoResumenMongo asiento) {
        this.asiento = asiento;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    
}
