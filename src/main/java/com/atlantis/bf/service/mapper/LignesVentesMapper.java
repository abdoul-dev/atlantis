package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.LignesVentesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LignesVentes} and its DTO {@link LignesVentesDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductsMapper.class, VentesMapper.class})
public interface LignesVentesMapper extends EntityMapper<LignesVentesDTO, LignesVentes> {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.libelle", target = "productsLibelle")
    @Mapping(source = "ventes.id", target = "ventesId")
    LignesVentesDTO toDto(LignesVentes lignesVentes);

    @Mapping(source = "productsId", target = "products")
    // @Mapping(target = "products", ignore = true)
    @Mapping(source = "ventesId", target = "ventes")
    LignesVentes toEntity(LignesVentesDTO lignesVentesDTO);

    default LignesVentes fromId(Long id) {
        if (id == null) {
            return null;
        }
        LignesVentes lignesVentes = new LignesVentes();
        lignesVentes.setId(id);
        return lignesVentes;
    }
}
