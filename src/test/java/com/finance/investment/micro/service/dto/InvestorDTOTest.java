package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvestorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvestorDTO.class);
        InvestorDTO investorDTO1 = new InvestorDTO();
        investorDTO1.setId(1L);
        InvestorDTO investorDTO2 = new InvestorDTO();
        assertThat(investorDTO1).isNotEqualTo(investorDTO2);
        investorDTO2.setId(investorDTO1.getId());
        assertThat(investorDTO1).isEqualTo(investorDTO2);
        investorDTO2.setId(2L);
        assertThat(investorDTO1).isNotEqualTo(investorDTO2);
        investorDTO1.setId(null);
        assertThat(investorDTO1).isNotEqualTo(investorDTO2);
    }
}
