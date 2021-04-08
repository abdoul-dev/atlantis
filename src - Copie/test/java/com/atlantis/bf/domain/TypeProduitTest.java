package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class TypeProduitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeProduit.class);
        TypeProduit typeProduit1 = new TypeProduit();
        typeProduit1.setId(1L);
        TypeProduit typeProduit2 = new TypeProduit();
        typeProduit2.setId(typeProduit1.getId());
        assertThat(typeProduit1).isEqualTo(typeProduit2);
        typeProduit2.setId(2L);
        assertThat(typeProduit1).isNotEqualTo(typeProduit2);
        typeProduit1.setId(null);
        assertThat(typeProduit1).isNotEqualTo(typeProduit2);
    }
}
