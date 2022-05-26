package com.finance.investment.micro.service;

import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.repository.InvestorRepository;
import com.finance.investment.micro.service.dto.InvestorDTO;
import com.finance.investment.micro.service.mapper.InvestorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Investor}.
 */
@Service
@Transactional
public class InvestorService {

    private final Logger log = LoggerFactory.getLogger(InvestorService.class);

    private final InvestorRepository investorRepository;

    private final InvestorMapper investorMapper;

    public InvestorService(InvestorRepository investorRepository, InvestorMapper investorMapper) {
        this.investorRepository = investorRepository;
        this.investorMapper = investorMapper;
    }

    /**
     * Save a investor.
     *
     * @param investorDTO the entity to save.
     * @return the persisted entity.
     */
    public InvestorDTO save(InvestorDTO investorDTO) {
        log.debug("Request to save Investor : {}", investorDTO);
        Investor investor = investorMapper.toEntity(investorDTO);
        investor = investorRepository.save(investor);
        return investorMapper.toDto(investor);
    }

    /**
     * Update a investor.
     *
     * @param investorDTO the entity to save.
     * @return the persisted entity.
     */
    public InvestorDTO update(InvestorDTO investorDTO) {
        log.debug("Request to save Investor : {}", investorDTO);
        Investor investor = investorMapper.toEntity(investorDTO);
        investor = investorRepository.save(investor);
        return investorMapper.toDto(investor);
    }

    /**
     * Partially update a investor.
     *
     * @param investorDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get all the investors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvestorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Investors");
        return investorRepository.findAll(pageable).map(investorMapper::toDto);
    }

    /**
     * Get one investor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvestorDTO> findOne(Long id) {
        log.debug("Request to get Investor : {}", id);
        return investorRepository.findById(id).map(investorMapper::toDto);
    }

    /**
     * Delete the investor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Investor : {}", id);
        investorRepository.deleteById(id);
    }
}
