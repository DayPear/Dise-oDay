package adapters;

import Entitys.Boleto;
import Entitys.ENUMS.EstadoBoleto;
import Entitys.Usuario;
import dtos.BoletoDTO;
import dtos.ENUMS.EstadoBoletoDTO;

/**
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class BoletoAdapter {

    public static BoletoDTO entidadADTO(Boleto boletoEntidad) {

        if (boletoEntidad == null) {
            return null;
        }

        return new BoletoDTO(
                boletoEntidad.getCodigoQR(),
                convertirEstadoADTO(boletoEntidad.getEstadoBoleto()),
                EventoAdapter.entidadADTO(boletoEntidad.getEvento()),
                AsientoEventoAdapter.entidadADTO(boletoEntidad.getAsiento()),
                boletoEntidad.getToken()
        );
    }

    public static Boleto dtoAEntidad(BoletoDTO boletoDTO) {

        if (boletoDTO == null) {
            return null;
        }

        Boleto boleto = new Boleto();

        boleto.setCodigoQR(boletoDTO.getCodigoQR());
        boleto.setEstadoBoleto(convertirEstadoAEntidad(boletoDTO.getEstadoBoleto()));
        boleto.setEvento(EventoAdapter.dtoAEntidad(boletoDTO.getEvento()));
        boleto.setAsiento(AsientoEventoAdapter.dtoAEntidad(boletoDTO.getAsiento()));
        boleto.setToken(boletoDTO.getToken());

        return boleto;
    }

    private static EstadoBoletoDTO convertirEstadoADTO(EstadoBoleto estadoEntidad) {

        if (estadoEntidad == null) {
            return null;
        }

        return EstadoBoletoDTO.valueOf(estadoEntidad.name());
    }

    private static EstadoBoleto convertirEstadoAEntidad(EstadoBoletoDTO estadoDTO) {

        if (estadoDTO == null) {
            return null;
        }

        return EstadoBoleto.valueOf(estadoDTO.name());
    }
}
