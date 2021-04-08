package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class LigneEntreeStockDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneEntreeStockDTO.class);
        LigneEntreeStockDTO ligneEntreeStockDTO1 = new LigneEntreeStockDTO();
        ligneEntreeStockDTO1.setId(1L);
        LigneEntreeStockDTO ligneEntreeStockDTO2 = new LigneEntreeStockDTO();
        assertThat(ligneEntreeStockDTO1).isNotEqualTo(ligneEntreeStockDTO2);
        ligneEntreeStockDTO2.setId(ligneEntreeStockDTO1.getId());
        assertThat(ligneEntreeStockDTO1).isEqualTo(ligneEntreeStockDTO2);
        ligneEntreeStockDTO2.setId(2L);
        assertThat(ligneEntreeStockDTO1).isNotEqualTo(ligneEntreeStockDTO2);
        ligneEntreeStockDTO1.setId(null);
        assertThat(ligneEntreeStockDTO1).isNotEqualTo(ligneEntreeStockDTO2);
    }
}
