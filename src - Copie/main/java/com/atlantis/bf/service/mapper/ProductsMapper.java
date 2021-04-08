package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.ProductsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Products} and its DTO {@link ProductsDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeProduitMapper.class})
public interface ProductsMapper extends EntityMapper<ProductsDTO, Products> {

    @Mapping(source = "typeProduit.id", target = "typeProduitId")
    @Mapping(source = "typeProduit.libelle", target = "typeProduitLibelle")
    ProductsDTO toDto(Products products);

    @Mapping(target = "ligneStocks", ignore = true)
    @Mapping(target = "removeLigneStock", ignore = true)
    @Mapping(target = "ligneVentes", ignore = true)
    @Mapping(target = "removeLigneVentes", ignore = true)
    @Mapping(target = "ligneReservations", ignore = true)
    @Mapping(target = "removeLigneReservation", ignore = true)
    @Mapping(source = "typeProduitId", target = "typeProduit")
    Products toEntity(ProductsDTO productsDTO);

    default Products fromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }
}
