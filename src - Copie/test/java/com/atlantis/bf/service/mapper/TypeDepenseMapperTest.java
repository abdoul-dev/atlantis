package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TypeDepenseMapperTest {

    private TypeDepenseMapper typeDepenseMapper;

    @BeforeEach
    public void setUp() {
        typeDepenseMapper = new TypeDepenseMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(typeDepenseMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(typeDepenseMapper.fromId(null)).isNull();
    }
}
