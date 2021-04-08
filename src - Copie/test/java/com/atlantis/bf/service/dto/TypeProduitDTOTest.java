package com.atlantis.bf.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class TypeProduitDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeProduitDTO.class);
        TypeProduitDTO typeProduitDTO1 = new TypeProduitDTO();
        typeProduitDTO1.setId(1L);
        TypeProduitDTO typeProduitDTO2 = new TypeProduitDTO();
        assertThat(typeProduitDTO1).isNotEqualTo(typeProduitDTO2);
        typeProduitDTO2.setId(typeProduitDTO1.getId());
        assertThat(typeProduitDTO1).isEqualTo(typeProduitDTO2);
        typeProduitDTO2.setId(2L);
        assertThat(typeProduitDTO1).isNotEqualTo(typeProduitDTO2);
        typeProduitDTO1.setId(null);
        assertThat(typeProduitDTO1).isNotEqualTo(typeProduitDTO2);
    }
}
