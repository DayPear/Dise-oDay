package Controlador.interfaz;

import dtos.AsientoDTO;
import dtos.AsientoEventoDTO;
import dtos.CategoriaDTO;
import dtos.CobroDTO;
import dtos.EventoDTO;
import dtos.LoginDTO;
import dtos.PagoDTO;
import dtos.RegistroUsuarioDTO;
import dtos.ReservacionDTO;
import dtos.SeccionDTO;
import dtos.TarjetaDTO;
import dtos.UsuarioDTO;
import dtos.UsuarioInstitucionalDTO;
import excepciones.CoordinadorException;
import excepciones.GestionEventoException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public interface ICoordinadorAplicacion {

    public void iniciaSistema();

    public void mostrarInicioSesion();

    public void mostrarRegistro();

    public void mostrarInicio();

    public void mostrarConsultar(String tipoEvento);

    public void mostrarConsultarMenu();

    public void mostrarInfoEvento(EventoDTO evento);

    public void mostrarDetalles(ReservacionDTO reservacion);

    public void mostarRegistroITSON();

    public void mostrarEventos(CategoriaDTO categoria);

    public void mostrarPago(ReservacionDTO reservacion);

    public void volverAConsultarEvento();

    public List<EventoDTO> consultarEventos(CategoriaDTO categoria) throws GestionEventoException;

    public List<CategoriaDTO> consultarCategorias();

    public List<ReservacionDTO> consultarReservaciones(String idUsuario);

    public Map<SeccionDTO, List<AsientoEventoDTO>> obtenerMapaOcupacion(EventoDTO evento);

    public List<AsientoDTO> obtenerCatalogoAsientos();

    public boolean agregarReservacion(ReservacionDTO reservacion) throws CoordinadorException;

    public UsuarioDTO iniciarSesion(LoginDTO login) throws CoordinadorException;

    public UsuarioDTO guardarUsuario(RegistroUsuarioDTO usuario) throws CoordinadorException;

    public boolean setUsuarioSesion(UsuarioDTO usuario);

    public UsuarioDTO getUsuarioSesion();

    public void cerrarSesion();

    boolean reservarAsiento(String idAsientoEvento);

    boolean liberarAsiento(String idAsientoEvento);

    public boolean venderAsientos(List<AsientoEventoDTO> asientosSeleccionados, Long totalCompra, boolean gratuito, ReservacionDTO reservacion);

    PagoDTO realizarCompra(TarjetaDTO noTarjeta, CobroDTO cobro);

    Long getTotalPendiente();

    String generarQR(EventoDTO evento, AsientoEventoDTO asiento, String token);

    boolean validarCredenciales(UsuarioInstitucionalDTO credenciales);
    
    boolean isUsuarioITSONRegistrado();
}
