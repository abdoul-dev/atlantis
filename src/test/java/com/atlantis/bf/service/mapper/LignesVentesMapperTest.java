package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LignesVentesMapperTest {

    private LignesVentesMapper lignesVentesMapper;

    @BeforeEach
    public void setUp() {
        lignesVentesMapper = new LignesVentesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(lignesVentesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(lignesVentesMapper.fromId(null)).isNull();
    }
}
