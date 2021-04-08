package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TypeProduitMapperTest {

    private TypeProduitMapper typeProduitMapper;

    @BeforeEach
    public void setUp() {
        typeProduitMapper = new TypeProduitMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(typeProduitMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(typeProduitMapper.fromId(null)).isNull();
    }
}
