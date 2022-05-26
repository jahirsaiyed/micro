package com.finance.investment.micro.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.investment.micro.domain.MasterDetails} entity.
 */
public class MasterDetailsDTO implements Serializable {

    private Long id;

    private BigDecimal balance;

    private BigDecimal totalUnits;

    private BigDecimal aum;

    private LocalDate createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(BigDecimal totalUnits) {
        this.totalUnits = totalUnits;
    }

    public BigDecimal getAum() {
        return aum;
    }

    public void setAum(BigDecimal aum) {
        this.aum = aum;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterDetailsDTO)) {
            return false;
        }

        MasterDetailsDTO masterDetailsDTO = (MasterDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, masterDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterDetailsDTO{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", totalUnits=" + getTotalUnits() +
            ", aum=" + getAum() +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
