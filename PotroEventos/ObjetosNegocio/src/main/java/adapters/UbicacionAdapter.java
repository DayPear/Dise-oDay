/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adapters;

import Entitys.ENUMS.TipoUbicacionP;
import Entitys.Ubicacion;
import dtos.ENUMS.TipoUbicacionN;
import dtos.UbicacionDTO;

/**
 *
 * @author maria
 */
public class UbicacionAdapter {
    
    public static UbicacionDTO entidadADTO(Ubicacion entidad){
        if(entidad == null){
            return null;
        }
        
        TipoUbicacionN tipoDTO = (entidad.getTipo() != null) 
                ? TipoUbicacionN.valueOf(entidad.getTipo().name()) 
                : null;
        
        return new UbicacionDTO(
                entidad.getIdUbicacion(),
                entidad.getNombre(),
                entidad.getCapacidad(),
                //TipoUbicacionN.valueOf(entidad.getTipo().name()),
                tipoDTO,
                SeccionAdapter.listaEntidadADTO(entidad.getSecciones())
        );
    }
    
    public static Ubicacion dtoAEntidad(UbicacionDTO dto){
        if(dto == null){
            return null;
        }
        
        TipoUbicacionP tipoEntidad = (dto.getTipo() != null) 
                ? TipoUbicacionP.valueOf(dto.getTipo().name()) 
                : null;
        
        return new Ubicacion(
                dto.getIdUbicacion(),
                dto.getNombre(),
                dto.getCapacidad(),
                //TipoUbicacionP.valueOf(dto.getTipo().name()),
                tipoEntidad,
                SeccionAdapter.listaDTOsAEntidades(dto.getSecciones())
        );
    }
}
