package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class EntreeStockTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntreeStock.class);
        EntreeStock entreeStock1 = new EntreeStock();
        entreeStock1.setId(1L);
        EntreeStock entreeStock2 = new EntreeStock();
        entreeStock2.setId(entreeStock1.getId());
        assertThat(entreeStock1).isEqualTo(entreeStock2);
        entreeStock2.setId(2L);
        assertThat(entreeStock1).isNotEqualTo(entreeStock2);
        entreeStock1.setId(null);
        assertThat(entreeStock1).isNotEqualTo(entreeStock2);
    }
}
