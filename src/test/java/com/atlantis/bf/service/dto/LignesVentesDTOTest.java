package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class LignesVentesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LignesVentesDTO.class);
        LignesVentesDTO lignesVentesDTO1 = new LignesVentesDTO();
        lignesVentesDTO1.setId(1L);
        LignesVentesDTO lignesVentesDTO2 = new LignesVentesDTO();
        assertThat(lignesVentesDTO1).isNotEqualTo(lignesVentesDTO2);
        lignesVentesDTO2.setId(lignesVentesDTO1.getId());
        assertThat(lignesVentesDTO1).isEqualTo(lignesVentesDTO2);
        lignesVentesDTO2.setId(2L);
        assertThat(lignesVentesDTO1).isNotEqualTo(lignesVentesDTO2);
        lignesVentesDTO1.setId(null);
        assertThat(lignesVentesDTO1).isNotEqualTo(lignesVentesDTO2);
    }
}
