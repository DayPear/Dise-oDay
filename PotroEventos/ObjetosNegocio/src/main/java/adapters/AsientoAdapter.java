package adapters;

import Entitys.Asiento;
import Entitys.Seccion;
import dtos.AsientoDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaron Burciaga - 262788
 * @author Brian Sandoval - 262741
 * @author Dayanara Peralta - 262695
 * @author María Valdez - 262775
 */
public class AsientoAdapter {

    public static AsientoDTO entidadADTO(Asiento asientoEntidad) {

        if (asientoEntidad == null) {
            return null;
        }

        return new AsientoDTO(
                asientoEntidad.getIdAsiento(),
                asientoEntidad.getFila(),
                asientoEntidad.getNumero(),
                UbicacionAdapter.entidadADTO(asientoEntidad.getUbicacion()),
                SeccionAdapter.entidadADTO(asientoEntidad.getSeccion())
        );
    }

    public static Asiento dtoAEntidad(AsientoDTO asientoDTO) {

        if (asientoDTO == null) {
            return null;
        }

        Asiento asiento = new Asiento();
        
        asiento.setIdAsiento(asientoDTO.getIdAsiento());
        asiento.setFila(asientoDTO.getFila());
        asiento.setNumero(asientoDTO.getNumero());
        asiento.setUbicacion(UbicacionAdapter.dtoAEntidad(asientoDTO.getUbicacion()));
        asiento.setSeccion(SeccionAdapter.dtoAEntidad(asientoDTO.getSeccion()));

        return asiento;
    }

    public static List<AsientoDTO> entidadesADTO(List<Asiento> entidades) {
        if (entidades == null) {
            return new ArrayList<>();
        }

        List<AsientoDTO> listaDTO = new ArrayList<>();

        for (Asiento asiento : entidades) {
            listaDTO.add(entidadADTO(asiento));
        }

        return listaDTO;
    }

    public static List<Asiento> dtosAEntidad(List<AsientoDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }

        List<Asiento> listaEntidad = new ArrayList<>();

        for (AsientoDTO dto : dtos) {
            listaEntidad.add(dtoAEntidad(dto));
        }

        return listaEntidad;
    }
}
