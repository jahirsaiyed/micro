package com.finance.investment.micro.repository;

import com.finance.investment.micro.domain.InvestorPortfolio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorPortfolioRepository extends JpaRepository<InvestorPortfolio, Long> {
    Optional<InvestorPortfolio> findByInvestor_Id(Long id);
}
