package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.LigneEntreeStockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LigneEntreeStock} and its DTO {@link LigneEntreeStockDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductsMapper.class, EntreeStockMapper.class})
public interface LigneEntreeStockMapper extends EntityMapper<LigneEntreeStockDTO, LigneEntreeStock> {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.libelle", target = "productsLibelle")
    @Mapping(source = "entreestock.id", target = "entreestockId")
    LigneEntreeStockDTO toDto(LigneEntreeStock ligneEntreeStock);

    @Mapping(source = "productsId", target = "products")
    @Mapping(source = "entreestockId", target = "entreestock")
    LigneEntreeStock toEntity(LigneEntreeStockDTO ligneEntreeStockDTO);

    default LigneEntreeStock fromId(Long id) {
        if (id == null) {
            return null;
        }
        LigneEntreeStock ligneEntreeStock = new LigneEntreeStock();
        ligneEntreeStock.setId(id);
        return ligneEntreeStock;
    }
}
