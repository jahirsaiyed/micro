package com.finance.investment.micro.listener;

import com.finance.investment.micro.service.InvestorPortfolioService;
import com.finance.investment.micro.service.InvestorService;
import com.finance.investment.micro.service.dto.InvestorDTO;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvestorCreationListener {

    private final InvestorService investorService;
    private final InvestorPortfolioService portfolioService;

    private final Logger log = LoggerFactory.getLogger(InvestorCreationListener.class);

    public InvestorCreationListener(InvestorService investorService, InvestorPortfolioService portfolioService) {
        this.investorService = investorService;
        this.portfolioService = portfolioService;
    }

    @EventListener
    public void handleInvestorEvent(InvestorDTO investorDTO) {
        InvestorPortfolioDTO portfolioDTO = new InvestorPortfolioDTO();
        portfolioDTO.setUnits(BigDecimal.ZERO);

        investorService.findOne(investorDTO.getId())
            .map(i -> {
                i.setPortfolio(portfolioService.save(portfolioDTO));
                return i;
            })
            .map(investorService::update);
    }

}
