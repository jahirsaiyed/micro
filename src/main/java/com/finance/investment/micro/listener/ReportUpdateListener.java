package com.finance.investment.micro.listener;

import com.finance.investment.micro.service.InvestorPortfolioService;
import com.finance.investment.micro.service.InvestorService;
import com.finance.investment.micro.service.MasterDetailsService;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import com.finance.investment.micro.service.dto.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReportUpdateListener {

    private final MasterDetailsService masterDetailsService;

    private final Logger log = LoggerFactory.getLogger(ReportUpdateListener.class);

    public ReportUpdateListener(MasterDetailsService masterDetailsService) {
        this.masterDetailsService = masterDetailsService;
    }

    @EventListener
    public void handleReportEvent(ReportDTO reportDTO) {
        masterDetailsService.find()
            .map(m -> {
                m.setBalance(reportDTO.getBalance());
                return m;
            })
            .map(masterDetailsService::save);
    }

}
