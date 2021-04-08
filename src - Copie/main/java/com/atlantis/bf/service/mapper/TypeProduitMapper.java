package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.TypeProduitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeProduit} and its DTO {@link TypeProduitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeProduitMapper extends EntityMapper<TypeProduitDTO, TypeProduit> {


    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProducts", ignore = true)
    TypeProduit toEntity(TypeProduitDTO typeProduitDTO);

    default TypeProduit fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeProduit typeProduit = new TypeProduit();
        typeProduit.setId(id);
        return typeProduit;
    }
}
