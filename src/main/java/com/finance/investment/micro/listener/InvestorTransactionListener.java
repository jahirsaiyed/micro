package com.finance.investment.micro.listener;

import com.finance.investment.micro.service.InvestorPortfolioService;
import com.finance.investment.micro.service.MasterDetailsService;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import com.finance.investment.micro.service.dto.MasterDetailsDTO;
import com.finance.investment.micro.service.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvestorTransactionListener {

    private final MasterDetailsService masterDetailsService;
    private final InvestorPortfolioService portfolioService;

    private final Logger log = LoggerFactory.getLogger(InvestorTransactionListener.class);

    public InvestorTransactionListener(MasterDetailsService masterDetailsService, InvestorPortfolioService portfolioService) {
        this.masterDetailsService = masterDetailsService;
        this.portfolioService = portfolioService;
    }

    @EventListener
    public void handleTransactionEvent(TransactionDTO transactionDTO) {
        MasterDetailsDTO masterDetailsDTOBefore = masterDetailsService.find().get();
        MasterDetailsDTO masterDetailsDTOAfter = masterDetailsService.updateMasterDetails(transactionDTO).get();
        Optional<InvestorPortfolioDTO> portfolioDTO = portfolioService.findByInvestorId(transactionDTO.getUser().getId());
        portfolioDTO
            .map(p -> {
                p.setUnits(p.getUnits().add(masterDetailsDTOAfter.getTotalUnits().subtract(masterDetailsDTOBefore.getTotalUnits())));
                return p;
            })
            .map(portfolioService::save);
    }

}
