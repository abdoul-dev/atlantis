package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.DepensesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Depenses} and its DTO {@link DepensesDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeDepenseMapper.class})
public interface DepensesMapper extends EntityMapper<DepensesDTO, Depenses> {

    @Mapping(source = "typeDepense.id", target = "typeDepenseId")
    @Mapping(source = "typeDepense.libelle", target = "typeDepenseLibelle")
    DepensesDTO toDto(Depenses depenses);

    @Mapping(source = "typeDepenseId", target = "typeDepense")
    Depenses toEntity(DepensesDTO depensesDTO);

    default Depenses fromId(Long id) {
        if (id == null) {
            return null;
        }
        Depenses depenses = new Depenses();
        depenses.setId(id);
        return depenses;
    }
}
