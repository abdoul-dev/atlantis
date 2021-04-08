package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.LignesReservationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LignesReservation} and its DTO {@link LignesReservationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductsMapper.class})
public interface LignesReservationMapper extends EntityMapper<LignesReservationDTO, LignesReservation> {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.libelle", target = "productsLibelle")
    LignesReservationDTO toDto(LignesReservation lignesReservation);

    @Mapping(source = "productsId", target = "products")
    LignesReservation toEntity(LignesReservationDTO lignesReservationDTO);

    default LignesReservation fromId(Long id) {
        if (id == null) {
            return null;
        }
        LignesReservation lignesReservation = new LignesReservation();
        lignesReservation.setId(id);
        return lignesReservation;
    }
}
