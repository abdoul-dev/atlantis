package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class LigneEntreeStockTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneEntreeStock.class);
        LigneEntreeStock ligneEntreeStock1 = new LigneEntreeStock();
        ligneEntreeStock1.setId(1L);
        LigneEntreeStock ligneEntreeStock2 = new LigneEntreeStock();
        ligneEntreeStock2.setId(ligneEntreeStock1.getId());
        assertThat(ligneEntreeStock1).isEqualTo(ligneEntreeStock2);
        ligneEntreeStock2.setId(2L);
        assertThat(ligneEntreeStock1).isNotEqualTo(ligneEntreeStock2);
        ligneEntreeStock1.setId(null);
        assertThat(ligneEntreeStock1).isNotEqualTo(ligneEntreeStock2);
    }
}
