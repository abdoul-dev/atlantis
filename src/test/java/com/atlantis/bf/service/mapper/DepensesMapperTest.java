package com.atlantis.bf.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DepensesMapperTest {

    private DepensesMapper depensesMapper;

    @BeforeEach
    public void setUp() {
        depensesMapper = new DepensesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(depensesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(depensesMapper.fromId(null)).isNull();
    }
}
