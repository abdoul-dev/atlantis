package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class DepensesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepensesDTO.class);
        DepensesDTO depensesDTO1 = new DepensesDTO();
        depensesDTO1.setId(1L);
        DepensesDTO depensesDTO2 = new DepensesDTO();
        assertThat(depensesDTO1).isNotEqualTo(depensesDTO2);
        depensesDTO2.setId(depensesDTO1.getId());
        assertThat(depensesDTO1).isEqualTo(depensesDTO2);
        depensesDTO2.setId(2L);
        assertThat(depensesDTO1).isNotEqualTo(depensesDTO2);
        depensesDTO1.setId(null);
        assertThat(depensesDTO1).isNotEqualTo(depensesDTO2);
    }
}
