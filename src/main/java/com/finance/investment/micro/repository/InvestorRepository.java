package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.Investor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Investor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {}
