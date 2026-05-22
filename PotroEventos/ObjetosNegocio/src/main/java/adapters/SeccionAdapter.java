package adapters;

import Entitys.Seccion;
import dtos.SeccionDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para convertir objetos Seccion a SeccionDTO y viceversa.
 */
public class SeccionAdapter {

    /**
     * Convierte una entidad Seccion a un DTO.
     *
     * @param entidad Objeto de base de datos.
     * @return DTO para la vista.
     */
    public static SeccionDTO entidadADTO(Seccion entidad) {
        if (entidad == null) {
            return null;
        }

        SeccionDTO dto = new SeccionDTO();
        dto.setIdSeccion(entidad.getIdSeccion());
        dto.setNombre(entidad.getNombre());
        dto.setPrecioBase(entidad.getPrecioBase());

        return dto;
    }

    /**
     * Convierte una lista de entidades Seccion a una lista de DTOs. Útil para
     * los métodos de los DAOs que regresan listas.
     */
    public static List<SeccionDTO> listaEntidadADTO(List<Seccion> entidades) {
        List<SeccionDTO> listaDtos = new ArrayList<>();
        if (entidades != null) {
            for (Seccion s : entidades) {
                listaDtos.add(entidadADTO(s));
            }
        }
        return listaDtos;
    }
    
    public static List<Seccion> listaDTOsAEntidades(List<SeccionDTO> dtos) {
        List<Seccion> listaEntidades = new ArrayList<>();
        if (dtos != null) {
            for (SeccionDTO s : dtos) {
                listaEntidades.add(dtoAEntidad(s));
            }
        }
        return listaEntidades;
    }

    /**
     * Convierte un DTO a una entidad Seccion (Útil para registros/updates).
     */
    public static Seccion dtoAEntidad(SeccionDTO dto) {
        if (dto == null) {
            return null;
        }

        Seccion entidad = new Seccion();
        entidad.setIdSeccion(dto.getIdSeccion());
        entidad.setNombre(dto.getNombre());
        entidad.setPrecioBase(dto.getPrecioBase());
        // La capacidad se suele manejar internamente en la lógica de negocio

        return entidad;
    }
}
