package com.finance.investment.micro.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MasterDetailsMapperTest {

    private MasterDetailsMapper masterDetailsMapper;

    @BeforeEach
    public void setUp() {
        masterDetailsMapper = new MasterDetailsMapperImpl();
    }
}
