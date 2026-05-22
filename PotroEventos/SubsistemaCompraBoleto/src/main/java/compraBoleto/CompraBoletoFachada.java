package compraBoleto;

import dtos.AsientoDTO;
import dtos.AsientoEventoDTO;
import dtos.BoletoDTO;
import dtos.CobroDTO;
import dtos.EventoDTO;
import dtos.PagoDTO;
import dtos.ReservacionDTO;
import dtos.SeccionDTO;
import dtos.TarjetaDTO;
import dtos.UsuarioInstitucionalDTO;
import excepciones.CompraBoletoException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kaleb
 */
public class CompraBoletoFachada implements ICompraBoleto {

    private ControlCompraBoleto controlCompra = ControlCompraBoleto.getInstance();

    @Override
    public List<SeccionDTO> obtenerSeccionesPorEvento(String idEvento) throws CompraBoletoException {
        return controlCompra.obtenerSeccionesEvento(idEvento);
    }

    @Override
    public List<AsientoDTO> obtenerAsientosPorSeccion(String idSeccion) throws CompraBoletoException {
        return controlCompra.obtenerCatalogoAsientos(); // ojo xq donde me meto el id? ahorita checas majo
    }

    @Override
    public List<AsientoEventoDTO> obtenerEstadoAsientosPorEvento(String idEvento) throws CompraBoletoException {
        return controlCompra.obtenerOcupacionEvento(idEvento);
    }

    @Override
    public boolean agregarReservacion(ReservacionDTO reservacion) throws CompraBoletoException {
        return controlCompra.agregarReservacion(reservacion);
    }

    @Override
    public List<AsientoDTO> obtenerCatalogoAsientos() throws CompraBoletoException {
        return controlCompra.obtenerCatalogoAsientos();
    }

    @Override
    public String generarCodigoQR(EventoDTO evento, AsientoEventoDTO asiento, String token) throws CompraBoletoException {
        return controlCompra.generarCodigoQR(evento, asiento, token);
    }

    @Override
    public boolean reservarAsiento(String idAsientoEvento) throws CompraBoletoException {
        return controlCompra.reservarAsiento(idAsientoEvento);
    }

    @Override
    public boolean liberarAsiento(String idAsientoEvento) throws CompraBoletoException {
        return controlCompra.liberarAsiento(idAsientoEvento);
    }

    @Override
    public boolean venderAsientos(List<AsientoEventoDTO> asientosSeleccionados, Long totalCompra, boolean gratuito, ReservacionDTO reservacion) throws CompraBoletoException {
        return controlCompra.venderAsientos(asientosSeleccionados, totalCompra, gratuito, reservacion);
    }

    @Override
    public PagoDTO realizarCompra(TarjetaDTO noTarjeta, CobroDTO cobro) throws CompraBoletoException {
        return controlCompra.realizarCompra(noTarjeta, cobro);
    }

    @Override
    public Long getTotalPendiente() {
        return controlCompra.getTotalPendiente();
    }

    @Override
    public Map<SeccionDTO, List<AsientoEventoDTO>> obtenerMapaOcupacion(EventoDTO evento) throws CompraBoletoException {
        return controlCompra.obtenerMapaOcupacion(evento);
    }
    
    @Override
    public boolean validarCredencialesITSON(UsuarioInstitucionalDTO usuario){
        return controlCompra.validarCredencialesITSON(usuario);
    }
}
