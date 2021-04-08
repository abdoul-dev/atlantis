package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FournisseurMapperTest {

    private FournisseurMapper fournisseurMapper;

    @BeforeEach
    public void setUp() {
        fournisseurMapper = new FournisseurMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fournisseurMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fournisseurMapper.fromId(null)).isNull();
    }
}
