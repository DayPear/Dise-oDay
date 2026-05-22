package adapters;

import Entitys.Categoria;
import Entitys.ENUMS.CategoriaEvento;
import Entitys.ENUMS.EstadoEvento;
import Entitys.ENUMS.TipoEventoP;
import Entitys.Evento;
import dtos.CategoriaDTO;
import dtos.ENUMS.CategoriaEventoDTO;
import dtos.ENUMS.EstadoEventoDTO;
import dtos.ENUMS.TipoEventoN;
import dtos.EventoDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para convertir entre Evento y EventoDTO. Se usan métodos estáticos
 * para evitar instanciación innecesaria.
 */
public class EventoAdapter {

    // Constructor privado para que nadie pueda hacer "new EventoAdapter()"
    private EventoAdapter() {
    }

    /**
     * Entidad -> DTO
     *
     * @param evento
     * @return
     */
    public static EventoDTO entidadADTO(Evento evento) {
        if (evento == null) {
            return null;
        }
  
        return new EventoDTO(evento.getIdEvento(), 
                CategoriaAdapter.entidadADTO(evento.getCategoriaEvento()),
                evento.getNombreEvento(), 
                evento.getInformacionEvento(), 
                evento.getFechaHora(), 
                UbicacionAdapter.entidadADTO(evento.getUbicacion()), 
                convertirEstadoADTO(evento.getEstadoEvento()), 
                evento.getUrlImagen(), 
                evento.isGratuito(), 
                TipoEventoN.valueOf(evento.getTipoEvento().name()), 
                evento.getDisponibilidad()
        );
    }

    /**
     * DTO -> Entidad
     *
     * @param dto
     * @return
     */
    public static Evento dtoAEntidad(EventoDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Evento(
                dto.getIdEvento(), 
                CategoriaAdapter.dtoAEntidad(dto.getCategoriaEvento()),
                dto.getNombreEvento(), 
                dto.getInformacionEvento(), 
                dto.getFechaHora(), 
                UbicacionAdapter.dtoAEntidad(dto.getUbicacion()), 
                convertirEstadoAEntidad(dto.getEstadoEvento()), 
                dto.getUrlImagen(), 
                dto.isGratuito(), 
                TipoEventoP.valueOf(dto.getTipoEvento().name()), 
                dto.getDisponibilidad()
        );
    }
    
    public static List<EventoDTO> listaDTOs(List<Evento> lista){
        List<EventoDTO> dtos = new ArrayList<>();
        for(Evento e : lista){
            dtos.add(entidadADTO(e));
        }
        return dtos;
    }

    private static EstadoEventoDTO convertirEstadoADTO(EstadoEvento estado) {
        return estado != null ? EstadoEventoDTO.valueOf(estado.name()) : null;
    }

    private static EstadoEvento convertirEstadoAEntidad(EstadoEventoDTO dto) {
        return dto != null ? EstadoEvento.valueOf(dto.name()) : null;
    }
}
