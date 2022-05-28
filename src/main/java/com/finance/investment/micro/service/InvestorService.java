package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.InvestorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.Investor}.
 */
public interface InvestorService {
    /**
     * Save a investor.
     *
     * @param investorDTO the entity to save.
     * @return the persisted entity.
     */
    InvestorDTO create(InvestorDTO investorDTO);

    /**
     * Updates a investor.
     *
     * @param investorDTO the entity to update.
     * @return the persisted entity.
     */
    InvestorDTO update(InvestorDTO investorDTO);

    /**
     * Partially updates a investor.
     *
     * @param investorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvestorDTO> partialUpdate(InvestorDTO investorDTO);

    /**
     * Get all the investors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvestorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" investor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvestorDTO> findOne(Long id);

    /**
     * Delete the "id" investor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
