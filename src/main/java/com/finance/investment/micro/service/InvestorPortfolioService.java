package com.finance.investment.micro.service;

import com.finance.investment.micro.domain.InvestorPortfolio;
import com.finance.investment.micro.repository.InvestorPortfolioRepository;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import com.finance.investment.micro.service.mapper.InvestorPortfolioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InvestorPortfolio}.
 */
@Service
@Transactional
public class InvestorPortfolioService {

    private final Logger log = LoggerFactory.getLogger(InvestorPortfolioService.class);

    private final InvestorPortfolioRepository investorPortfolioRepository;

    private final InvestorPortfolioMapper investorPortfolioMapper;

    public InvestorPortfolioService(
        InvestorPortfolioRepository investorPortfolioRepository,
        InvestorPortfolioMapper investorPortfolioMapper
    ) {
        this.investorPortfolioRepository = investorPortfolioRepository;
        this.investorPortfolioMapper = investorPortfolioMapper;
    }

    /**
     * Save a investorPortfolio.
     *
     * @param investorPortfolioDTO the entity to save.
     * @return the persisted entity.
     */
    public InvestorPortfolioDTO save(InvestorPortfolioDTO investorPortfolioDTO) {
        log.debug("Request to save InvestorPortfolio : {}", investorPortfolioDTO);
        InvestorPortfolio investorPortfolio = investorPortfolioMapper.toEntity(investorPortfolioDTO);
        investorPortfolio = investorPortfolioRepository.save(investorPortfolio);
        return investorPortfolioMapper.toDto(investorPortfolio);
    }

    /**
     * Update a investorPortfolio.
     *
     * @param investorPortfolioDTO the entity to save.
     * @return the persisted entity.
     */
    public InvestorPortfolioDTO update(InvestorPortfolioDTO investorPortfolioDTO) {
        log.debug("Request to save InvestorPortfolio : {}", investorPortfolioDTO);
        InvestorPortfolio investorPortfolio = investorPortfolioMapper.toEntity(investorPortfolioDTO);
        investorPortfolio = investorPortfolioRepository.save(investorPortfolio);
        return investorPortfolioMapper.toDto(investorPortfolio);
    }

    /**
     * Partially update a investorPortfolio.
     *
     * @param investorPortfolioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvestorPortfolioDTO> partialUpdate(InvestorPortfolioDTO investorPortfolioDTO) {
        log.debug("Request to partially update InvestorPortfolio : {}", investorPortfolioDTO);

        return investorPortfolioRepository
            .findById(investorPortfolioDTO.getId())
            .map(existingInvestorPortfolio -> {
                investorPortfolioMapper.partialUpdate(existingInvestorPortfolio, investorPortfolioDTO);

                return existingInvestorPortfolio;
            })
            .map(investorPortfolioRepository::save)
            .map(investorPortfolioMapper::toDto);
    }

    /**
     * Get all the investorPortfolios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvestorPortfolioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvestorPortfolios");
        return investorPortfolioRepository.findAll(pageable).map(investorPortfolioMapper::toDto);
    }

    /**
     *  Get all the investorPortfolios where Investor is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvestorPortfolioDTO> findAllWhereInvestorIsNull() {
        log.debug("Request to get all investorPortfolios where Investor is null");
        return StreamSupport
            .stream(investorPortfolioRepository.findAll().spliterator(), false)
            .filter(investorPortfolio -> investorPortfolio.getInvestor() == null)
            .map(investorPortfolioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one investorPortfolio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvestorPortfolioDTO> findOne(Long id) {
        log.debug("Request to get InvestorPortfolio : {}", id);
        return investorPortfolioRepository.findById(id).map(investorPortfolioMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<InvestorPortfolioDTO> findInvestor(Long id) {
        log.debug("Request to get InvestorPortfolio : investorid {}", id);
        return investorPortfolioRepository.findByInvestor_Id(id).map(investorPortfolioMapper::toDto);
    }

    /**
     * Delete the investorPortfolio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvestorPortfolio : {}", id);
        investorPortfolioRepository.deleteById(id);
    }
}
