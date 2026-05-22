package objetosNegocio;

import adapters.BoletoAdapter;
import adapters.ReservacionAdapter;
import daos.ReservacionDAO;
import dtos.BoletoDTO;
import dtos.ReservacionDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IReservacionBO;
import interfaces.IReservacionDAO;
import java.util.List;

/**
 *
 * @author maria
 */
public class ReservacionBO implements IReservacionBO {

    private static ReservacionBO instance;
    private final IReservacionDAO reservacionDAO = ReservacionDAO.getInstance();

    private ReservacionBO() {
    }

    public static ReservacionBO getInstance() {
        if (instance == null) {
            instance = new ReservacionBO();
        }
        return instance;
    }

    @Override
    public boolean agregarReservacion(ReservacionDTO reservacion) throws NegocioException {
        try {
            if (!validarDatos(reservacion)) {
                throw new NegocioException("Reservación inválida.");
            }
            return reservacionDAO.guardarReservacion(ReservacionAdapter.dtoAEntidad(reservacion));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public List<ReservacionDTO> obtenerReservacionesUsuario(String idUsuario) throws NegocioException {
        try {
            if (idUsuario == null) {
                throw new NegocioException("ID usuario inválido.");
            }
            return ReservacionAdapter.listaDTOs(reservacionDAO.obtenerReservacionesUsuario(idUsuario));
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    private boolean validarDatos(ReservacionDTO r) {

        if (r == null) {
            return false;
        }

        if (r.getBoleto() == null) {
            return false;
        }

        if (r.getEstado() == null) {
            return false;
        }

        if (r.getFechaHora() == null) {
            return false;
        }

        if (r.getTotal() == null) {
            return false;
        }

        if (r.getUsuario() == null) {
            return false;
        }

        return true;
    }

    @Override
    public BoletoDTO obtenerBoleto(String idReservacion) throws NegocioException{
        try{
            if(idReservacion == null){
                throw new NegocioException("Id reserva inválido");
            }
            return BoletoAdapter.entidadADTO(reservacionDAO.obtenerBoleto(idReservacion));
        }catch(PersistenciaException e){
            throw new NegocioException(e.getMessage());
        }
    }
    
}
