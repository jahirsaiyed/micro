package com.finance.investment.micro.service;

import com.finance.investment.micro.service.dto.MasterDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.investment.micro.domain.MasterDetails}.
 */
public interface MasterDetailsService {
    /**
     * Save a masterDetails.
     *
     * @param masterDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    MasterDetailsDTO save(MasterDetailsDTO masterDetailsDTO);

    /**
     * Updates a masterDetails.
     *
     * @param masterDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    MasterDetailsDTO update(MasterDetailsDTO masterDetailsDTO);

    /**
     * Partially updates a masterDetails.
     *
     * @param masterDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MasterDetailsDTO> partialUpdate(MasterDetailsDTO masterDetailsDTO);

    /**
     * Get all the masterDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MasterDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" masterDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MasterDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" masterDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
