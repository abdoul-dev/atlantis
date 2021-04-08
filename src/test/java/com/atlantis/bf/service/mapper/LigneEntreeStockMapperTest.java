package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LigneEntreeStockMapperTest {

    private LigneEntreeStockMapper ligneEntreeStockMapper;

    @BeforeEach
    public void setUp() {
        ligneEntreeStockMapper = new LigneEntreeStockMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ligneEntreeStockMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ligneEntreeStockMapper.fromId(null)).isNull();
    }
}
