package com.atlantis.bf.service.mapper;


import com.atlantis.bf.domain.*;
import com.atlantis.bf.service.dto.ReservationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.fullName", target = "clientFullName")
    ReservationDTO toDto(Reservation reservation);

    @Mapping(source = "clientId", target = "client")
    Reservation toEntity(ReservationDTO reservationDTO);

    default Reservation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(id);
        return reservation;
    }
}
