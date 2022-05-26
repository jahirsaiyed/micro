package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvestorPortfolioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvestorPortfolio.class);
        InvestorPortfolio investorPortfolio1 = new InvestorPortfolio();
        investorPortfolio1.setId(1L);
        InvestorPortfolio investorPortfolio2 = new InvestorPortfolio();
        investorPortfolio2.setId(investorPortfolio1.getId());
        assertThat(investorPortfolio1).isEqualTo(investorPortfolio2);
        investorPortfolio2.setId(2L);
        assertThat(investorPortfolio1).isNotEqualTo(investorPortfolio2);
        investorPortfolio1.setId(null);
        assertThat(investorPortfolio1).isNotEqualTo(investorPortfolio2);
    }
}
