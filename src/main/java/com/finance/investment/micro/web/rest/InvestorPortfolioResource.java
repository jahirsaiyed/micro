package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.InvestorPortfolioRepository;
import com.finance.investment.micro.service.InvestorPortfolioService;
import com.finance.investment.micro.service.dto.InvestorPortfolioDTO;
import com.finance.investment.micro.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.finance.investment.micro.domain.InvestorPortfolio}.
 */
@RestController
@RequestMapping("/api")
public class InvestorPortfolioResource {

    private final Logger log = LoggerFactory.getLogger(InvestorPortfolioResource.class);

    private static final String ENTITY_NAME = "investorPortfolio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvestorPortfolioService investorPortfolioService;

    private final InvestorPortfolioRepository investorPortfolioRepository;

    public InvestorPortfolioResource(
        InvestorPortfolioService investorPortfolioService,
        InvestorPortfolioRepository investorPortfolioRepository
    ) {
        this.investorPortfolioService = investorPortfolioService;
        this.investorPortfolioRepository = investorPortfolioRepository;
    }

    /**
     * {@code POST  /investor-portfolios} : Create a new investorPortfolio.
     *
     * @param investorPortfolioDTO the investorPortfolioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new investorPortfolioDTO, or with status {@code 400 (Bad Request)} if the investorPortfolio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/investor-portfolios")
    public ResponseEntity<InvestorPortfolioDTO> createInvestorPortfolio(@RequestBody InvestorPortfolioDTO investorPortfolioDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvestorPortfolio : {}", investorPortfolioDTO);
        if (investorPortfolioDTO.getId() != null) {
            throw new BadRequestAlertException("A new investorPortfolio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvestorPortfolioDTO result = investorPortfolioService.save(investorPortfolioDTO);
        return ResponseEntity
            .created(new URI("/api/investor-portfolios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /investor-portfolios/:id} : Updates an existing investorPortfolio.
     *
     * @param id the id of the investorPortfolioDTO to save.
     * @param investorPortfolioDTO the investorPortfolioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investorPortfolioDTO,
     * or with status {@code 400 (Bad Request)} if the investorPortfolioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the investorPortfolioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/investor-portfolios/{id}")
    public ResponseEntity<InvestorPortfolioDTO> updateInvestorPortfolio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvestorPortfolioDTO investorPortfolioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvestorPortfolio : {}, {}", id, investorPortfolioDTO);
        if (investorPortfolioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investorPortfolioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investorPortfolioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvestorPortfolioDTO result = investorPortfolioService.update(investorPortfolioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investorPortfolioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /investor-portfolios/:id} : Partial updates given fields of an existing investorPortfolio, field will ignore if it is null
     *
     * @param id the id of the investorPortfolioDTO to save.
     * @param investorPortfolioDTO the investorPortfolioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investorPortfolioDTO,
     * or with status {@code 400 (Bad Request)} if the investorPortfolioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the investorPortfolioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the investorPortfolioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/investor-portfolios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvestorPortfolioDTO> partialUpdateInvestorPortfolio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvestorPortfolioDTO investorPortfolioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvestorPortfolio partially : {}, {}", id, investorPortfolioDTO);
        if (investorPortfolioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investorPortfolioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investorPortfolioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvestorPortfolioDTO> result = investorPortfolioService.partialUpdate(investorPortfolioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investorPortfolioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /investor-portfolios} : get all the investorPortfolios.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of investorPortfolios in body.
     */
    @GetMapping("/investor-portfolios")
    public ResponseEntity<List<InvestorPortfolioDTO>> getAllInvestorPortfolios(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("investor-is-null".equals(filter)) {
            log.debug("REST request to get all InvestorPortfolios where investor is null");
            return new ResponseEntity<>(investorPortfolioService.findAllWhereInvestorIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of InvestorPortfolios");
        Page<InvestorPortfolioDTO> page = investorPortfolioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /investor-portfolios/:id} : get the "id" investorPortfolio.
     *
     * @param id the id of the investorPortfolioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the investorPortfolioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/investor-portfolios/{id}")
    public ResponseEntity<InvestorPortfolioDTO> getInvestorPortfolio(@PathVariable Long id) {
        log.debug("REST request to get InvestorPortfolio : {}", id);
        Optional<InvestorPortfolioDTO> investorPortfolioDTO = investorPortfolioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(investorPortfolioDTO);
    }

    /**
     * {@code DELETE  /investor-portfolios/:id} : delete the "id" investorPortfolio.
     *
     * @param id the id of the investorPortfolioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/investor-portfolios/{id}")
    public ResponseEntity<Void> deleteInvestorPortfolio(@PathVariable Long id) {
        log.debug("REST request to delete InvestorPortfolio : {}", id);
        investorPortfolioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
