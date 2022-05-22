package com.finance.investment.micro.service;

import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.repository.InvestorRepository;
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

    public InvestorService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    /**
     * Save a investor.
     *
     * @param investor the entity to save.
     * @return the persisted entity.
     */
    public Investor save(Investor investor) {
        log.debug("Request to save Investor : {}", investor);
        return investorRepository.save(investor);
    }

    /**
     * Update a investor.
     *
     * @param investor the entity to save.
     * @return the persisted entity.
     */
    public Investor update(Investor investor) {
        log.debug("Request to save Investor : {}", investor);
        return investorRepository.save(investor);
    }

    /**
     * Partially update a investor.
     *
     * @param investor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Investor> partialUpdate(Investor investor) {
        log.debug("Request to partially update Investor : {}", investor);

        return investorRepository
            .findById(investor.getId())
            .map(existingInvestor -> {
                if (investor.getName() != null) {
                    existingInvestor.setName(investor.getName());
                }
                if (investor.getDescription() != null) {
                    existingInvestor.setDescription(investor.getDescription());
                }
                if (investor.getEmail() != null) {
                    existingInvestor.setEmail(investor.getEmail());
                }
                if (investor.getGender() != null) {
                    existingInvestor.setGender(investor.getGender());
                }
                if (investor.getPhone() != null) {
                    existingInvestor.setPhone(investor.getPhone());
                }
                if (investor.getAddressLine1() != null) {
                    existingInvestor.setAddressLine1(investor.getAddressLine1());
                }
                if (investor.getAddressLine2() != null) {
                    existingInvestor.setAddressLine2(investor.getAddressLine2());
                }
                if (investor.getCity() != null) {
                    existingInvestor.setCity(investor.getCity());
                }
                if (investor.getCountry() != null) {
                    existingInvestor.setCountry(investor.getCountry());
                }
                if (investor.getCreatedOn() != null) {
                    existingInvestor.setCreatedOn(investor.getCreatedOn());
                }

                return existingInvestor;
            })
            .map(investorRepository::save);
    }

    /**
     * Get all the investors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Investor> findAll(Pageable pageable) {
        log.debug("Request to get all Investors");
        return investorRepository.findAll(pageable);
    }

    /**
     * Get one investor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Investor> findOne(Long id) {
        log.debug("Request to get Investor : {}", id);
        return investorRepository.findById(id);
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
