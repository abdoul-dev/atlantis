package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class DepensesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Depenses.class);
        Depenses depenses1 = new Depenses();
        depenses1.setId(1L);
        Depenses depenses2 = new Depenses();
        depenses2.setId(depenses1.getId());
        assertThat(depenses1).isEqualTo(depenses2);
        depenses2.setId(2L);
        assertThat(depenses1).isNotEqualTo(depenses2);
        depenses1.setId(null);
        assertThat(depenses1).isNotEqualTo(depenses2);
    }
}
