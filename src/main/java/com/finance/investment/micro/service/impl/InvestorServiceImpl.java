package com.finance.investment.micro.service.impl;

import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.repository.InvestorRepository;
import com.finance.investment.micro.service.InvestorService;
import com.finance.investment.micro.service.dto.InvestorDTO;
import com.finance.investment.micro.service.mapper.InvestorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InvestorServiceImpl implements InvestorService {

    private final Logger log = LoggerFactory.getLogger(InvestorServiceImpl.class);

    private final InvestorRepository investorRepository;

    private final InvestorMapper investorMapper;

    private final ApplicationEventPublisher eventPublisher;

    public InvestorServiceImpl(InvestorRepository investorRepository, InvestorMapper investorMapper, ApplicationEventPublisher eventPublisher) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public InvestorDTO create(InvestorDTO investorDTO) {
        log.debug("Request to save Investor : {}", investorDTO);
        InvestorDTO saved = save(investorDTO);
        eventPublisher.publishEvent(saved);
        return saved;
    }

    private InvestorDTO save(InvestorDTO investorDTO) {
        log.debug("Request to save Investor : {}", investorDTO);
        Investor investor = investorMapper.toEntity(investorDTO);
        investor = investorRepository.save(investor);
        return investorMapper.toDto(investor);
    }

    @Override
    public InvestorDTO update(InvestorDTO investorDTO) {
        log.debug("Request to save Investor : {}", investorDTO);
        return save(investorDTO);
    }

    @Override
    public Optional<InvestorDTO> partialUpdate(InvestorDTO investorDTO) {
        log.debug("Request to partially update Investor : {}", investorDTO);

        return investorRepository
            .findById(investorDTO.getId())
            .map(existingInvestor -> {
                investorMapper.partialUpdate(existingInvestor, investorDTO);

                return existingInvestor;
            })
            .map(investorRepository::save)
            .map(investorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvestorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Investors");
        return investorRepository.findAll(pageable).map(investorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvestorDTO> findOne(Long id) {
        log.debug("Request to get Investor : {}", id);
        return investorRepository.findById(id).map(investorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Investor : {}", id);
        investorRepository.deleteById(id);
    }
}
