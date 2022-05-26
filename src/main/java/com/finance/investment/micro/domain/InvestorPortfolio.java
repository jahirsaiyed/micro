package com.finance.investment.micro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvestorPortfolio.
 */
@Entity
@Table(name = "investor_portfolio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InvestorPortfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "units", precision = 21, scale = 2)
    private BigDecimal units;

    @JsonIgnoreProperties(value = { "portfolio" }, allowSetters = true)
    @OneToOne(mappedBy = "portfolio")
    private Investor investor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvestorPortfolio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnits() {
        return this.units;
    }

    public InvestorPortfolio units(BigDecimal units) {
        this.setUnits(units);
        return this;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public Investor getInvestor() {
        return this.investor;
    }

    public void setInvestor(Investor investor) {
        if (this.investor != null) {
            this.investor.setPortfolio(null);
        }
        if (investor != null) {
            investor.setPortfolio(this);
        }
        this.investor = investor;
    }

    public InvestorPortfolio investor(Investor investor) {
        this.setInvestor(investor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestorPortfolio)) {
            return false;
        }
        return id != null && id.equals(((InvestorPortfolio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestorPortfolio{" +
            "id=" + getId() +
            ", units=" + getUnits() +
            "}";
    }
}
