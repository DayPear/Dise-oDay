package Entitys;

import Entitys.ENUMS.EstadoBoleto;

/**
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class Boleto {

    private String idBoleto;
    private String codigoQR;
    private Double precio;
    private EstadoBoleto estadoBoleto;
    private Evento evento;
    private AsientoEvento asiento;
    private String token;

    public Boleto() {
    }

    public Boleto(String idBoleto, String codigoQR, Double precio, EstadoBoleto estadoBoleto, Evento evento, AsientoEvento asiento, String token) {
        this.idBoleto = idBoleto;
        this.codigoQR = codigoQR;
        this.precio = precio;
        this.estadoBoleto = estadoBoleto;
        this.evento = evento;
        this.asiento = asiento;
        this.token = token;
    }

    public String getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(String idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public EstadoBoleto getEstadoBoleto() {
        return estadoBoleto;
    }

    public void setEstadoBoleto(EstadoBoleto estadoBoleto) {
        this.estadoBoleto = estadoBoleto;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public AsientoEvento getAsiento() {
        return asiento;
    }

    public void setAsiento(AsientoEvento asiento) {
        this.asiento = asiento;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
}
