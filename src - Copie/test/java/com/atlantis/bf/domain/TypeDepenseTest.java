package com.atlantis.bf.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlantis.bf.web.rest.TestUtil;

public class TypeDepenseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDepense.class);
        TypeDepense typeDepense1 = new TypeDepense();
        typeDepense1.setId(1L);
        TypeDepense typeDepense2 = new TypeDepense();
        typeDepense2.setId(typeDepense1.getId());
        assertThat(typeDepense1).isEqualTo(typeDepense2);
        typeDepense2.setId(2L);
        assertThat(typeDepense1).isNotEqualTo(typeDepense2);
        typeDepense1.setId(null);
        assertThat(typeDepense1).isNotEqualTo(typeDepense2);
    }
}
