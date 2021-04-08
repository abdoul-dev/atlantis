package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class EntreeStockDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntreeStockDTO.class);
        EntreeStockDTO entreeStockDTO1 = new EntreeStockDTO();
        entreeStockDTO1.setId(1L);
        EntreeStockDTO entreeStockDTO2 = new EntreeStockDTO();
        assertThat(entreeStockDTO1).isNotEqualTo(entreeStockDTO2);
        entreeStockDTO2.setId(entreeStockDTO1.getId());
        assertThat(entreeStockDTO1).isEqualTo(entreeStockDTO2);
        entreeStockDTO2.setId(2L);
        assertThat(entreeStockDTO1).isNotEqualTo(entreeStockDTO2);
        entreeStockDTO1.setId(null);
        assertThat(entreeStockDTO1).isNotEqualTo(entreeStockDTO2);
    }
}
