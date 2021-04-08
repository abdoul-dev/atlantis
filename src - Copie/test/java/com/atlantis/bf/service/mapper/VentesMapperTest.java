package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VentesMapperTest {

    private VentesMapper ventesMapper;

    @BeforeEach
    public void setUp() {
        ventesMapper = new VentesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ventesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ventesMapper.fromId(null)).isNull();
    }
}
