package com.finance.investment.micro.web.rest;

import com.finance.investment.micro.repository.InvestorRepository;
import com.finance.investment.micro.service.InvestorService;
import com.finance.investment.micro.service.dto.InvestorDTO;
import com.finance.investment.micro.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.finance.investment.micro.domain.Investor}.
 */
@RestController
@RequestMapping("/api")
public class InvestorResource {

    private final Logger log = LoggerFactory.getLogger(InvestorResource.class);

    private static final String ENTITY_NAME = "investor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvestorService investorService;

    private final InvestorRepository investorRepository;

    public InvestorResource(InvestorService investorService, InvestorRepository investorRepository) {
        this.investorService = investorService;
        this.investorRepository = investorRepository;
    }

    /**
     * {@code POST  /investors} : Create a new investor.
     *
     * @param investorDTO the investorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new investorDTO, or with status {@code 400 (Bad Request)} if the investor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/investors")
    public ResponseEntity<InvestorDTO> createInvestor(@Valid @RequestBody InvestorDTO investorDTO) throws URISyntaxException {
        log.debug("REST request to save Investor : {}", investorDTO);
        if (investorDTO.getId() != null) {
            throw new BadRequestAlertException("A new investor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvestorDTO result = investorService.create(investorDTO);
        return ResponseEntity
            .created(new URI("/api/investors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /investors/:id} : Updates an existing investor.
     *
     * @param id the id of the investorDTO to save.
     * @param investorDTO the investorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investorDTO,
     * or with status {@code 400 (Bad Request)} if the investorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the investorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/investors/{id}")
    public ResponseEntity<InvestorDTO> updateInvestor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvestorDTO investorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Investor : {}, {}", id, investorDTO);
        if (investorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvestorDTO result = investorService.update(investorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /investors/:id} : Partial updates given fields of an existing investor, field will ignore if it is null
     *
     * @param id the id of the investorDTO to save.
     * @param investorDTO the investorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investorDTO,
     * or with status {@code 400 (Bad Request)} if the investorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the investorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the investorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/investors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvestorDTO> partialUpdateInvestor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvestorDTO investorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Investor partially : {}, {}", id, investorDTO);
        if (investorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvestorDTO> result = investorService.partialUpdate(investorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /investors} : get all the investors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of investors in body.
     */
    @GetMapping("/investors")
    public ResponseEntity<List<InvestorDTO>> getAllInvestors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Investors");
        Page<InvestorDTO> page = investorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /investors/:id} : get the "id" investor.
     *
     * @param id the id of the investorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the investorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/investors/{id}")
    public ResponseEntity<InvestorDTO> getInvestor(@PathVariable Long id) {
        log.debug("REST request to get Investor : {}", id);
        Optional<InvestorDTO> investorDTO = investorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(investorDTO);
    }

    /**
     * {@code DELETE  /investors/:id} : delete the "id" investor.
     *
     * @param id the id of the investorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/investors/{id}")
    public ResponseEntity<Void> deleteInvestor(@PathVariable Long id) {
        log.debug("REST request to delete Investor : {}", id);
        investorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
