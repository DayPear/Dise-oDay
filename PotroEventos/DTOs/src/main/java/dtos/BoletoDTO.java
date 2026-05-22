package dtos;

import dtos.ENUMS.EstadoBoletoDTO;
import java.time.LocalDate;

/**
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class BoletoDTO {

    private String codigoQR;
    private EstadoBoletoDTO estadoBoleto;
    private EventoDTO evento;
    private AsientoEventoDTO asiento;
    private String token;

    public BoletoDTO() {
    }

    public BoletoDTO(String codigoQR, EstadoBoletoDTO estadoBoleto, EventoDTO evento, AsientoEventoDTO asiento, String token) {
        this.codigoQR = codigoQR;
        this.estadoBoleto = estadoBoleto;
        this.evento = evento;
        this.asiento = asiento;
        this.token = token;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public EstadoBoletoDTO getEstadoBoleto() {
        return estadoBoleto;
    }

    public void setEstadoBoleto(EstadoBoletoDTO estadoBoleto) {
        this.estadoBoleto = estadoBoleto;
    }

    public EventoDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoDTO evento) {
        this.evento = evento;
    }

    public AsientoEventoDTO getAsiento() {
        return asiento;
    }

    public void setAsiento(AsientoEventoDTO asiento) {
        this.asiento = asiento;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
