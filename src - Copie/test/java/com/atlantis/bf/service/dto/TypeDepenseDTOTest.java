package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class TypeDepenseDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDepenseDTO.class);
        TypeDepenseDTO typeDepenseDTO1 = new TypeDepenseDTO();
        typeDepenseDTO1.setId(1L);
        TypeDepenseDTO typeDepenseDTO2 = new TypeDepenseDTO();
        assertThat(typeDepenseDTO1).isNotEqualTo(typeDepenseDTO2);
        typeDepenseDTO2.setId(typeDepenseDTO1.getId());
        assertThat(typeDepenseDTO1).isEqualTo(typeDepenseDTO2);
        typeDepenseDTO2.setId(2L);
        assertThat(typeDepenseDTO1).isNotEqualTo(typeDepenseDTO2);
        typeDepenseDTO1.setId(null);
        assertThat(typeDepenseDTO1).isNotEqualTo(typeDepenseDTO2);
    }
}
