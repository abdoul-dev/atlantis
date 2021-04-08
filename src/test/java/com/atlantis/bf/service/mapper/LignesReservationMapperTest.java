package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LignesReservationMapperTest {

    private LignesReservationMapper lignesReservationMapper;

    @BeforeEach
    public void setUp() {
        lignesReservationMapper = new LignesReservationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(lignesReservationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(lignesReservationMapper.fromId(null)).isNull();
    }
}
