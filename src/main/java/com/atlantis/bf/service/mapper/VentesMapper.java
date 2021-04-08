package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.VentesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ventes} and its DTO {@link VentesDTO}.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, LignesVentesMapper.class})
public interface VentesMapper extends EntityMapper<VentesDTO, Ventes> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.fullName", target = "clientFullName")
    VentesDTO toDto(Ventes ventes);

    // @Mapping(source = "lignesVentes", target = "lignesVentes")
    //@Mapping(target = "clientFullName", ignore = true)
    @Mapping(target = "lignesVentes", ignore = true)
    @Mapping(target = "removeLignesVentes", ignore = true)
    @Mapping(source = "clientId", target = "client")
    Ventes toEntity(VentesDTO ventesDTO);

    default Ventes fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ventes ventes = new Ventes();
        ventes.setId(id);
        return ventes;
    }
}
