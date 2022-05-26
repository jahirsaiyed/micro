package com.finance.investment.micro.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvestorMapperTest {

    private InvestorMapper investorMapper;

    @BeforeEach
    public void setUp() {
        investorMapper = new InvestorMapperImpl();
    }
}
