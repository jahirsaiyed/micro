package com.finance.investment.micro.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.investment.micro.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterDetails.class);
        MasterDetails masterDetails1 = new MasterDetails();
        masterDetails1.setId(1L);
        MasterDetails masterDetails2 = new MasterDetails();
        masterDetails2.setId(masterDetails1.getId());
        assertThat(masterDetails1).isEqualTo(masterDetails2);
        masterDetails2.setId(2L);
        assertThat(masterDetails1).isNotEqualTo(masterDetails2);
        masterDetails1.setId(null);
        assertThat(masterDetails1).isNotEqualTo(masterDetails2);
    }
}
