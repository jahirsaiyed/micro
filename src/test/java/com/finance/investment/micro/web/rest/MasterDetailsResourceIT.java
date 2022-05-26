package com.finance.investment.micro.web.rest;

import static com.finance.investment.micro.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.MasterDetails;
import com.finance.investment.micro.repository.MasterDetailsRepository;
import com.finance.investment.micro.service.dto.MasterDetailsDTO;
import com.finance.investment.micro.service.mapper.MasterDetailsMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MasterDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MasterDetailsResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_UNITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_UNITS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_AUM = new BigDecimal(2);

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/master-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MasterDetailsRepository masterDetailsRepository;

    @Autowired
    private MasterDetailsMapper masterDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterDetailsMockMvc;

    private MasterDetails masterDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterDetails createEntity(EntityManager em) {
        MasterDetails masterDetails = new MasterDetails()
            .balance(DEFAULT_BALANCE)
            .totalUnits(DEFAULT_TOTAL_UNITS)
            .aum(DEFAULT_AUM)
            .createdOn(DEFAULT_CREATED_ON);
        return masterDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterDetails createUpdatedEntity(EntityManager em) {
        MasterDetails masterDetails = new MasterDetails()
            .balance(UPDATED_BALANCE)
            .totalUnits(UPDATED_TOTAL_UNITS)
            .aum(UPDATED_AUM)
            .createdOn(UPDATED_CREATED_ON);
        return masterDetails;
    }

    @BeforeEach
    public void initTest() {
        masterDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createMasterDetails() throws Exception {
        int databaseSizeBeforeCreate = masterDetailsRepository.findAll().size();
        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);
        restMasterDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        MasterDetails testMasterDetails = masterDetailsList.get(masterDetailsList.size() - 1);
        assertThat(testMasterDetails.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testMasterDetails.getTotalUnits()).isEqualByComparingTo(DEFAULT_TOTAL_UNITS);
        assertThat(testMasterDetails.getAum()).isEqualByComparingTo(DEFAULT_AUM);
        assertThat(testMasterDetails.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    void createMasterDetailsWithExistingId() throws Exception {
        // Create the MasterDetails with an existing ID
        masterDetails.setId(1L);
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        int databaseSizeBeforeCreate = masterDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMasterDetails() throws Exception {
        // Initialize the database
        masterDetailsRepository.saveAndFlush(masterDetails);

        // Get all the masterDetailsList
        restMasterDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].totalUnits").value(hasItem(sameNumber(DEFAULT_TOTAL_UNITS))))
            .andExpect(jsonPath("$.[*].aum").value(hasItem(sameNumber(DEFAULT_AUM))))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @Test
    @Transactional
    void getMasterDetails() throws Exception {
        // Initialize the database
        masterDetailsRepository.saveAndFlush(masterDetails);

        // Get the masterDetails
        restMasterDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, masterDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterDetails.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.totalUnits").value(sameNumber(DEFAULT_TOTAL_UNITS)))
            .andExpect(jsonPath("$.aum").value(sameNumber(DEFAULT_AUM)))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMasterDetails() throws Exception {
        // Get the masterDetails
        restMasterDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMasterDetails() throws Exception {
        // Initialize the database
        masterDetailsRepository.saveAndFlush(masterDetails);

        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();

        // Update the masterDetails
        MasterDetails updatedMasterDetails = masterDetailsRepository.findById(masterDetails.getId()).get();
        // Disconnect from session so that the updates on updatedMasterDetails are not directly saved in db
        em.detach(updatedMasterDetails);
        updatedMasterDetails.balance(UPDATED_BALANCE).totalUnits(UPDATED_TOTAL_UNITS).aum(UPDATED_AUM).createdOn(UPDATED_CREATED_ON);
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(updatedMasterDetails);

        restMasterDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
        MasterDetails testMasterDetails = masterDetailsList.get(masterDetailsList.size() - 1);
        assertThat(testMasterDetails.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testMasterDetails.getTotalUnits()).isEqualByComparingTo(UPDATED_TOTAL_UNITS);
        assertThat(testMasterDetails.getAum()).isEqualByComparingTo(UPDATED_AUM);
        assertThat(testMasterDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingMasterDetails() throws Exception {
        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();
        masterDetails.setId(count.incrementAndGet());

        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterDetails() throws Exception {
        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();
        masterDetails.setId(count.incrementAndGet());

        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterDetails() throws Exception {
        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();
        masterDetails.setId(count.incrementAndGet());

        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterDetailsWithPatch() throws Exception {
        // Initialize the database
        masterDetailsRepository.saveAndFlush(masterDetails);

        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();

        // Update the masterDetails using partial update
        MasterDetails partialUpdatedMasterDetails = new MasterDetails();
        partialUpdatedMasterDetails.setId(masterDetails.getId());

        partialUpdatedMasterDetails.balance(UPDATED_BALANCE);

        restMasterDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterDetails))
            )
            .andExpect(status().isOk());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
        MasterDetails testMasterDetails = masterDetailsList.get(masterDetailsList.size() - 1);
        assertThat(testMasterDetails.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testMasterDetails.getTotalUnits()).isEqualByComparingTo(DEFAULT_TOTAL_UNITS);
        assertThat(testMasterDetails.getAum()).isEqualByComparingTo(DEFAULT_AUM);
        assertThat(testMasterDetails.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateMasterDetailsWithPatch() throws Exception {
        // Initialize the database
        masterDetailsRepository.saveAndFlush(masterDetails);

        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();

        // Update the masterDetails using partial update
        MasterDetails partialUpdatedMasterDetails = new MasterDetails();
        partialUpdatedMasterDetails.setId(masterDetails.getId());

        partialUpdatedMasterDetails.balance(UPDATED_BALANCE).totalUnits(UPDATED_TOTAL_UNITS).aum(UPDATED_AUM).createdOn(UPDATED_CREATED_ON);

        restMasterDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterDetails))
            )
            .andExpect(status().isOk());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
        MasterDetails testMasterDetails = masterDetailsList.get(masterDetailsList.size() - 1);
        assertThat(testMasterDetails.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testMasterDetails.getTotalUnits()).isEqualByComparingTo(UPDATED_TOTAL_UNITS);
        assertThat(testMasterDetails.getAum()).isEqualByComparingTo(UPDATED_AUM);
        assertThat(testMasterDetails.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingMasterDetails() throws Exception {
        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();
        masterDetails.setId(count.incrementAndGet());

        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterDetails() throws Exception {
        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();
        masterDetails.setId(count.incrementAndGet());

        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterDetails() throws Exception {
        int databaseSizeBeforeUpdate = masterDetailsRepository.findAll().size();
        masterDetails.setId(count.incrementAndGet());

        // Create the MasterDetails
        MasterDetailsDTO masterDetailsDTO = masterDetailsMapper.toDto(masterDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterDetails in the database
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterDetails() throws Exception {
        // Initialize the database
        masterDetailsRepository.saveAndFlush(masterDetails);

        int databaseSizeBeforeDelete = masterDetailsRepository.findAll().size();

        // Delete the masterDetails
        restMasterDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MasterDetails> masterDetailsList = masterDetailsRepository.findAll();
        assertThat(masterDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
