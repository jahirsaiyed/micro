package com.finance.investment.micro.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvestorPortfolioMapperTest {

    private InvestorPortfolioMapper investorPortfolioMapper;

    @BeforeEach
    public void setUp() {
        investorPortfolioMapper = new InvestorPortfolioMapperImpl();
    }
}
