package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterDetailsDTO.class);
        MasterDetailsDTO masterDetailsDTO1 = new MasterDetailsDTO();
        masterDetailsDTO1.setId(1L);
        MasterDetailsDTO masterDetailsDTO2 = new MasterDetailsDTO();
        assertThat(masterDetailsDTO1).isNotEqualTo(masterDetailsDTO2);
        masterDetailsDTO2.setId(masterDetailsDTO1.getId());
        assertThat(masterDetailsDTO1).isEqualTo(masterDetailsDTO2);
        masterDetailsDTO2.setId(2L);
        assertThat(masterDetailsDTO1).isNotEqualTo(masterDetailsDTO2);
        masterDetailsDTO1.setId(null);
        assertThat(masterDetailsDTO1).isNotEqualTo(masterDetailsDTO2);
    }
}
