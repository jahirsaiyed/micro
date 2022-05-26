package com.finance.investment.micro.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.InvestorPortfolio} entity.
 */
public class InvestorPortfolioDTO implements Serializable {

    private Long id;

    private BigDecimal units;

    private InvestorDTO investorDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public InvestorDTO getInvestorDTO() {
        return investorDTO;
    }

    public void setInvestorDTO(InvestorDTO investorDTO) {
        this.investorDTO = investorDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestorPortfolioDTO that = (InvestorPortfolioDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(units, that.units) && Objects.equals(investorDTO, that.investorDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, units, investorDTO);
    }

    @Override
    public String toString() {
        return "InvestorPortfolioDTO{" +
            "id=" + id +
            ", units=" + units +
            ", investorDTO=" + investorDTO +
            '}';
    }
}
