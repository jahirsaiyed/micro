package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.InvestorPortfolio}.
 */
public interface InvestorPortfolioService {
    /**
     * Save a investorPortfolio.
     *
     * @param investorPortfolioDTO the entity to save.
     * @return the persisted entity.
     */
    InvestorPortfolioDTO save(InvestorPortfolioDTO investorPortfolioDTO);

    /**
     * Updates a investorPortfolio.
     *
     * @param investorPortfolioDTO the entity to update.
     * @return the persisted entity.
     */
    InvestorPortfolioDTO update(InvestorPortfolioDTO investorPortfolioDTO);

    /**
     * Partially updates a investorPortfolio.
     *
     * @param investorPortfolioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvestorPortfolioDTO> partialUpdate(InvestorPortfolioDTO investorPortfolioDTO);

    /**
     * Get all the investorPortfolios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvestorPortfolioDTO> findAll(Pageable pageable);
    /**
     * Get all the InvestorPortfolioDTO where Investor is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InvestorPortfolioDTO> findAllWhereInvestorIsNull();

    /**
     * Get the "id" investorPortfolio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvestorPortfolioDTO> findOne(Long id);

    /**
     * Delete the "id" investorPortfolio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
