package Controlador.coordinador;

import Controlador.interfaz.ICoordinadorAplicacion;
import Pantallas.FrmInicioSesion;
import Pantallas.FrmPago;
import Pantallas.FrmPlantillaSistema;
import Pantallas.FrmRegistrarse;
import Pantallas.FrmDetallesCompra;
import Pantallas.FrmRegistroItson;
import Pantallas.vistas.PnlConsultar;
import Pantallas.vistas.PnlConsultarEvento;
import Pantallas.vistas.PnlConsultarMenu;
import Pantallas.vistas.PnlEventos;
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
import inicioSesion.InicioSesionFachada;
import excepciones.CompraBoletoException;
import excepciones.CoordinadorException;
import excepciones.GestionEventoException;
import excepciones.GestionUsuarioException;
import excepciones.InicioSesionException;
import compraBoleto.CompraBoletoFachada;
import gestionEvento.GestionEventoFachada;
import gestionUsuarios.GestionUsuarioFachada;
import gestionEvento.IFachadaGestionEvento;
import inicioSesion.IFachadaInicioSesion;
import gestionUsuarios.IGestionUsuariosFachada;
import compraBoleto.ICompraBoleto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Clase que actúa como coordinador principal de la aplicación. Se encarga de
 * gestionar la navegación entre las diferentes pantallas y de comunicar la capa
 * de presentación (vistas) con la capa de negocio (fachadas y BOs).
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class CoordinadorAplicacion implements ICoordinadorAplicacion {

    private final IFachadaInicioSesion controlInicioSesion = InicioSesionFachada.getInstance();
    private final ICompraBoleto controlCompra = new CompraBoletoFachada();
    private final IFachadaGestionEvento controlEvento = new GestionEventoFachada();
    private final IGestionUsuariosFachada controlUsuarios = new GestionUsuarioFachada();

    private FrmInicioSesion frmInicioSesion;
    private FrmRegistrarse frmRegistrarse;
    private FrmPago frmPago;
    private FrmPlantillaSistema frmPlantilla;
    private FrmDetallesCompra frmDetalles;
    private FrmRegistroItson frmRegistro;
    private ReservacionDTO reservacionActual;

    /**
     * Oculta todas las ventanas instanciadas actualmente en el sistema. Es
     * utilizado como un paso previo antes de mostrar una nueva pantalla para
     * asegurar que no queden ventanas superpuestas.
     */
    private void ocultarTodo() {
        if (frmInicioSesion != null) {
            frmInicioSesion.setVisible(false);
        }
        if (frmRegistrarse != null) {
            frmRegistrarse.setVisible(false);
        }
        if (frmPago != null) {
            frmPago.setVisible(false);
        }
        if (frmDetalles != null) {
            frmDetalles.setVisible(false);
        }
        if (frmRegistro != null) {
            frmRegistro.setVisible(false);
        }
        if (frmPlantilla != null) {
            frmPlantilla.setVisible(false);
        }
    }

    @Override
    public void iniciaSistema() {
        if (frmInicioSesion == null) {
            frmInicioSesion = new FrmInicioSesion(this);
        }
        frmInicioSesion.setVisible(true);
        frmInicioSesion.setLocationRelativeTo(null);
    }

    @Override
    public void mostrarInicioSesion() {
        ocultarTodo();
        if (frmPlantilla != null) {
            frmPlantilla.dispose();
            frmPlantilla = null;
        }
        if (frmInicioSesion == null) {
            frmInicioSesion = new FrmInicioSesion(this);
        }
        frmInicioSesion.limpiarCampos();
        frmInicioSesion.setVisible(true);
        frmInicioSesion.setLocationRelativeTo(null);
    }

    @Override
    public void mostrarRegistro() {
        ocultarTodo();
        if (frmRegistrarse == null) {
            frmRegistrarse = new FrmRegistrarse(this);
        }
        frmRegistrarse.setVisible(true);
        frmRegistrarse.setLocationRelativeTo(null);
    }

    @Override
    public void mostrarInicio() {
        ocultarTodo();
        if (frmPlantilla == null) {
            frmPlantilla = new FrmPlantillaSistema(this);
        }
        frmPlantilla.ocultarInicio();
        frmPlantilla.mostrarConsultar();
        frmPlantilla.setCategorias();
        frmPlantilla.setVisible(true);
        if (frmInicioSesion != null) {
            frmInicioSesion.dispose();
        }
    }

    @Override
    public void mostrarConsultar(String tipoEvento) {
        ocultarTodo();
        if (frmPlantilla == null) {
            frmPlantilla = new FrmPlantillaSistema(this);
        }
        frmPlantilla.ocultarConsultar();
        frmPlantilla.mostrarInicio();
        frmPlantilla.setContenido(new PnlConsultar(this, tipoEvento));
        frmPlantilla.setVisible(true);
    }

    @Override
    public void mostrarConsultarMenu() {
        ocultarTodo();
        if (frmPlantilla == null) {
            frmPlantilla = new FrmPlantillaSistema(this);
        }
        frmPlantilla.ocultarConsultar();
        frmPlantilla.mostrarInicio();
        frmPlantilla.setContenido(new PnlConsultarMenu(this));
        frmPlantilla.setVisible(true);
    }

    @Override
    public void mostrarInfoEvento(EventoDTO evento) {
        ocultarTodo();
        if (frmPlantilla == null) {
            frmPlantilla = new FrmPlantillaSistema(this);
        }
        frmPlantilla.mostrarConsultar();
        frmPlantilla.mostrarInicio();
        frmPlantilla.setContenido(new PnlConsultarEvento(this, evento));
        frmPlantilla.setVisible(true);
    }

    @Override
    public void mostrarDetalles(ReservacionDTO reservacion) {
        ocultarTodo();
        if (frmDetalles == null) {
            frmDetalles = new FrmDetallesCompra(this, reservacion);
        } else {
            frmDetalles.setReservacion(reservacion);
        }
        frmDetalles.setVisible(true);
    }

    @Override
    public void mostarRegistroITSON() {
        ocultarTodo();
        frmRegistro.setVisible(true);
    }

    @Override
    public void mostrarEventos(CategoriaDTO categoria) {
        ocultarTodo();
        if (frmPlantilla == null) {
            frmPlantilla = new FrmPlantillaSistema(this);
        }
        if (frmRegistro == null) {
            frmRegistro = new FrmRegistroItson(this, categoria);
        }

        try {
            frmPlantilla.setContenido(new PnlEventos(this, categoria));
        } catch (GestionEventoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        frmPlantilla.mostrarInicio();
        frmPlantilla.mostrarConsultar();
        frmPlantilla.setVisible(true);
    }

    @Override
    public List<EventoDTO> consultarEventos(CategoriaDTO categoria) throws GestionEventoException {
        return controlEvento.consultarEventosPorCategoria(categoria);
    }

    @Override
    public List<CategoriaDTO> consultarCategorias() {
        try {
            return controlEvento.consultarCategorias();
        } catch (GestionEventoException ex) {
            System.out.println("Fallo al consultar categorías: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public UsuarioDTO iniciarSesion(LoginDTO login) throws CoordinadorException {
        try {
            UsuarioDTO usuarioDTO = controlInicioSesion.iniciarSesion(login);
            setUsuarioSesion(usuarioDTO);
            return usuarioDTO;
        } catch (InicioSesionException ex) {
            throw new CoordinadorException(ex.getMessage());
        }
    }

    @Override
    public boolean setUsuarioSesion(UsuarioDTO usuario) {
        try {
            return controlUsuarios.asociarUsuario(usuario);
        } catch (GestionUsuarioException gue) {
            return false;
        }
    }

    @Override
    public UsuarioDTO getUsuarioSesion() {
        try {
            return controlUsuarios.obtenerUsuarioActivo();
        } catch (GestionUsuarioException gue) {
            return null;
        }
    }

    @Override
    public void cerrarSesion() {
        controlInicioSesion.cerrarSesion();
        this.mostrarInicioSesion();
    }

    @Override
    public List<ReservacionDTO> consultarReservaciones(String idUsuario) {
        try {
            return controlUsuarios.obtenerReservacionesUsuario(idUsuario);
        } catch (GestionUsuarioException ex) {
            System.out.println("Fallo al consultar reservaciones: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean agregarReservacion(ReservacionDTO reservacion) throws CoordinadorException {
        try {
            return controlCompra.agregarReservacion(reservacion);
        } catch (CompraBoletoException ex) {
            throw new CoordinadorException(ex.getMessage());
        }
    }

    /**
     * Construye un mapa estructurado que relaciona cada sección del evento con
     * la lista de sus asientos y su estado de ocupación respectivo. Utiliza el
     * catálogo de asientos para saber a qué sección pertenece cada asiento y
     * evitar duplicados en la interfaz gráfica.
     *
     * @param idEvento El identificador único del evento a consultar.
     * @return Un mapa que vincula objetos SeccionDTO con listas de
     * AsientoEventoDTO.
     */
    @Override
    public Map<SeccionDTO, List<AsientoEventoDTO>> obtenerMapaOcupacion(EventoDTO evento) {
        try {
            return controlCompra.obtenerMapaOcupacion(evento);
        } catch (CompraBoletoException e) {
            System.err.println(e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Obtiene el catálogo completo de asientos disponibles en el sistema. Sirve
     * como base técnica para conocer la estructura de filas y números
     * independientemente del evento.
     *
     * @return Una lista de objetos AsientoDTO. Retorna una lista vacía en caso
     * de error.
     */
    @Override
    public List<AsientoDTO> obtenerCatalogoAsientos() {
        try {
            return controlCompra.obtenerAsientosPorSeccion(null);
        } catch (CompraBoletoException ex) {
            System.err.println("Error al obtener catálogo de asientos: " + ex.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public boolean reservarAsiento(String idAsientoEvento) {
        try {
            return controlCompra.reservarAsiento(idAsientoEvento);
        } catch (CompraBoletoException ex) {
            return false;
        }
    }

    @Override
    public boolean liberarAsiento(String idAsientoEvento) {
        try {
            return controlCompra.liberarAsiento(idAsientoEvento);
        } catch (CompraBoletoException ex) {
            return false;
        }
    }

    @Override
    public boolean venderAsientos(List<AsientoEventoDTO> asientosSeleccionados, Long totalCompra, boolean gratuito, ReservacionDTO reservacion) {
        try {
            return controlCompra.venderAsientos(asientosSeleccionados, totalCompra, gratuito, reservacion);
        } catch (CompraBoletoException ex) {
            return false;
        }
    }

    @Override
    public PagoDTO realizarCompra(TarjetaDTO tarjeta, CobroDTO cobro) {
        try {
            return controlCompra.realizarCompra(tarjeta, cobro);
        } catch (CompraBoletoException ex) {
            return null;
        }
    }

    @Override
    public Long getTotalPendiente() {
        return controlCompra.getTotalPendiente();
    }

    @Override
    public void volverAConsultarEvento() {
        if (frmPago != null) {
            frmPago.dispose();
        }
        frmRegistro.dispose();
        frmPlantilla.setVisible(true);
    }

    @Override
    public String generarQR(EventoDTO evento, AsientoEventoDTO asiento, String token) {
        try {
            return controlCompra.generarCodigoQR(evento, asiento, token);
        } catch (CompraBoletoException ex) {
            return null;
        }
    }

    @Override
    public void mostrarPago(ReservacionDTO reservacion) {
        ocultarTodo();

        this.reservacionActual = reservacion;

        frmPago = new FrmPago(this, reservacionActual);
        frmPago.setVisible(true);
        frmPago.setLocationRelativeTo(null);
    }

    @Override
    public boolean validarCredenciales(UsuarioInstitucionalDTO credenciales) {
        return controlCompra.validarCredencialesITSON(credenciales);
    }

    @Override
    public UsuarioDTO guardarUsuario(RegistroUsuarioDTO usuario) throws CoordinadorException {
        try {
            return controlInicioSesion.registrarUsuario(usuario);
        } catch (InicioSesionException ex) {
            throw new CoordinadorException(ex.getMessage());
        }
    }

    @Override
    public boolean isUsuarioITSONRegistrado() {
        return frmRegistro.registroExitoso();
    }

}
