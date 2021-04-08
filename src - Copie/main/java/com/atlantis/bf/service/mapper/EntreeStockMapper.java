package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.EntreeStockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EntreeStock} and its DTO {@link EntreeStockDTO}.
 */
@Mapper(componentModel = "spring", uses = {FournisseurMapper.class})
public interface EntreeStockMapper extends EntityMapper<EntreeStockDTO, EntreeStock> {

    @Mapping(source = "fournisseur.id", target = "fournisseurId")
    @Mapping(source = "fournisseur.fullName", target = "fournisseurFullName")
    EntreeStockDTO toDto(EntreeStock entreeStock);

    @Mapping(target = "ligneEntreeStocks", ignore = true)
    @Mapping(target = "removeLigneEntreeStock", ignore = true)
    @Mapping(source = "fournisseurId", target = "fournisseur")
    EntreeStock toEntity(EntreeStockDTO entreeStockDTO);

    default EntreeStock fromId(Long id) {
        if (id == null) {
            return null;
        }
        EntreeStock entreeStock = new EntreeStock();
        entreeStock.setId(id);
        return entreeStock;
    }
}
