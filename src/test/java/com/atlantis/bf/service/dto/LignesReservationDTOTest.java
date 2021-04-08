package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class LignesReservationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LignesReservationDTO.class);
        LignesReservationDTO lignesReservationDTO1 = new LignesReservationDTO();
        lignesReservationDTO1.setId(1L);
        LignesReservationDTO lignesReservationDTO2 = new LignesReservationDTO();
        assertThat(lignesReservationDTO1).isNotEqualTo(lignesReservationDTO2);
        lignesReservationDTO2.setId(lignesReservationDTO1.getId());
        assertThat(lignesReservationDTO1).isEqualTo(lignesReservationDTO2);
        lignesReservationDTO2.setId(2L);
        assertThat(lignesReservationDTO1).isNotEqualTo(lignesReservationDTO2);
        lignesReservationDTO1.setId(null);
        assertThat(lignesReservationDTO1).isNotEqualTo(lignesReservationDTO2);
    }
}
