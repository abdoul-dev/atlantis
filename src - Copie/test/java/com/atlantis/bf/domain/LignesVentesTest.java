package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class LignesVentesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LignesVentes.class);
        LignesVentes lignesVentes1 = new LignesVentes();
        lignesVentes1.setId(1L);
        LignesVentes lignesVentes2 = new LignesVentes();
        lignesVentes2.setId(lignesVentes1.getId());
        assertThat(lignesVentes1).isEqualTo(lignesVentes2);
        lignesVentes2.setId(2L);
        assertThat(lignesVentes1).isNotEqualTo(lignesVentes2);
        lignesVentes1.setId(null);
        assertThat(lignesVentes1).isNotEqualTo(lignesVentes2);
    }
}
