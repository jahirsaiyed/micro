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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestorPortfolioDTO)) {
            return false;
        }

        InvestorPortfolioDTO investorPortfolioDTO = (InvestorPortfolioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, investorPortfolioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestorPortfolioDTO{" +
            "id=" + getId() +
            ", units=" + getUnits() +
            "}";
    }
}
