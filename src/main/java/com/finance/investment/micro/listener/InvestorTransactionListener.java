package com.finance.investment.micro.listener;

import com.finance.investment.micro.domain.enumeration.TransactionType;
import com.finance.investment.micro.service.InvestorPortfolioService;
import com.finance.investment.micro.service.MasterDetailsService;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import com.finance.investment.micro.service.dto.MasterDetailsDTO;
import com.finance.investment.micro.service.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
        MasterDetailsDTO masterDetailsDTOAfter = updateMasterDetails(transactionDTO).get();
        Optional<InvestorPortfolioDTO> portfolioDTOBefore = portfolioService.findByInvestorId(transactionDTO.getUser().getId());
        InvestorPortfolioDTO portfolioAfter = portfolioDTOBefore
            .map(p -> {
                p.setUnits(p.getUnits().add(masterDetailsDTOAfter.getTotalUnits().subtract(masterDetailsDTOBefore.getTotalUnits())));
                p.setInvestments(transactionDTO.getType() == TransactionType.DEPOSIT ?
                    p.getInvestments().add(transactionDTO.getAmount()) : p.getInvestments().subtract(transactionDTO.getAmount()));
                return p;
            })
            .map(portfolioService::save).get();

        masterDetailsDTOAfter.setAum(transactionDTO.getType() == TransactionType.DEPOSIT ?
            masterDetailsDTOAfter.getAum().add(portfolioAfter.getInvestments())
            : masterDetailsDTOAfter.getAum().subtract(portfolioAfter.getInvestments()));
        masterDetailsService.save(masterDetailsDTOAfter);
    }

    private Optional<MasterDetailsDTO> updateMasterDetails(TransactionDTO transactionDTO) {
        return masterDetailsService.find()
            .map(m -> {
                BigDecimal units = transactionDTO.getAmount().multiply(m.getTotalUnits()).divide(m.getBalance(), new MathContext(4, RoundingMode.FLOOR));
                switch (transactionDTO.getType()) {
                    case DEPOSIT:
                        m.setBalance(m.getBalance().add(transactionDTO.getAmount()));
                        m.setTotalUnits(m.getTotalUnits().add(units));
                        break;
                    case WITHDRAWAL:
                        m.setBalance(m.getBalance().subtract(transactionDTO.getAmount()));
                        m.setTotalUnits(m.getTotalUnits().subtract(units));
                        break;
                }
                return m;
            });
    }

}
