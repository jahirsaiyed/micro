package com.finance.investment.micro.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MasterDetails.
 */
@Entity
@Table(name = "master_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MasterDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "balance", precision = 21, scale = 2)
    private BigDecimal balance;

    @Column(name = "total_units", precision = 21, scale = 2)
    private BigDecimal totalUnits;

    @Column(name = "aum", precision = 21, scale = 2)
    private BigDecimal aum;

    @Column(name = "created_on")
    private LocalDate createdOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MasterDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public MasterDetails balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalUnits() {
        return this.totalUnits;
    }

    public MasterDetails totalUnits(BigDecimal totalUnits) {
        this.setTotalUnits(totalUnits);
        return this;
    }

    public void setTotalUnits(BigDecimal totalUnits) {
        this.totalUnits = totalUnits;
    }

    public BigDecimal getAum() {
        return this.aum;
    }

    public MasterDetails aum(BigDecimal aum) {
        this.setAum(aum);
        return this;
    }

    public void setAum(BigDecimal aum) {
        this.aum = aum;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public MasterDetails createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterDetails)) {
            return false;
        }
        return id != null && id.equals(((MasterDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterDetails{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", totalUnits=" + getTotalUnits() +
            ", aum=" + getAum() +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
