package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class LignesReservationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LignesReservation.class);
        LignesReservation lignesReservation1 = new LignesReservation();
        lignesReservation1.setId(1L);
        LignesReservation lignesReservation2 = new LignesReservation();
        lignesReservation2.setId(lignesReservation1.getId());
        assertThat(lignesReservation1).isEqualTo(lignesReservation2);
        lignesReservation2.setId(2L);
        assertThat(lignesReservation1).isNotEqualTo(lignesReservation2);
        lignesReservation1.setId(null);
        assertThat(lignesReservation1).isNotEqualTo(lignesReservation2);
    }
}
