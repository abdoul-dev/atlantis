package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EntreeStockMapperTest {

    private EntreeStockMapper entreeStockMapper;

    @BeforeEach
    public void setUp() {
        entreeStockMapper = new EntreeStockMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(entreeStockMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(entreeStockMapper.fromId(null)).isNull();
    }
}
