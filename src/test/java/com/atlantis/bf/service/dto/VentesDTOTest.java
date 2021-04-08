package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class VentesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentesDTO.class);
        VentesDTO ventesDTO1 = new VentesDTO();
        ventesDTO1.setId(1L);
        VentesDTO ventesDTO2 = new VentesDTO();
        assertThat(ventesDTO1).isNotEqualTo(ventesDTO2);
        ventesDTO2.setId(ventesDTO1.getId());
        assertThat(ventesDTO1).isEqualTo(ventesDTO2);
        ventesDTO2.setId(2L);
        assertThat(ventesDTO1).isNotEqualTo(ventesDTO2);
        ventesDTO1.setId(null);
        assertThat(ventesDTO1).isNotEqualTo(ventesDTO2);
    }
}
