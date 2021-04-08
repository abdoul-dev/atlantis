package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class VentesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ventes.class);
        Ventes ventes1 = new Ventes();
        ventes1.setId(1L);
        Ventes ventes2 = new Ventes();
        ventes2.setId(ventes1.getId());
        assertThat(ventes1).isEqualTo(ventes2);
        ventes2.setId(2L);
        assertThat(ventes1).isNotEqualTo(ventes2);
        ventes1.setId(null);
        assertThat(ventes1).isNotEqualTo(ventes2);
    }
}
