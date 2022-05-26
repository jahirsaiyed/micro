package com.finance.investment.micro.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvestorPortfolioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvestorPortfolioDTO.class);
        InvestorPortfolioDTO investorPortfolioDTO1 = new InvestorPortfolioDTO();
        investorPortfolioDTO1.setId(1L);
        InvestorPortfolioDTO investorPortfolioDTO2 = new InvestorPortfolioDTO();
        assertThat(investorPortfolioDTO1).isNotEqualTo(investorPortfolioDTO2);
        investorPortfolioDTO2.setId(investorPortfolioDTO1.getId());
        assertThat(investorPortfolioDTO1).isEqualTo(investorPortfolioDTO2);
        investorPortfolioDTO2.setId(2L);
        assertThat(investorPortfolioDTO1).isNotEqualTo(investorPortfolioDTO2);
        investorPortfolioDTO1.setId(null);
        assertThat(investorPortfolioDTO1).isNotEqualTo(investorPortfolioDTO2);
    }
}
