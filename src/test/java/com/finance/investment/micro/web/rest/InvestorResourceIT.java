package com.finance.investment.micro.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.finance.investment.micro.IntegrationTest;
import com.finance.investment.micro.domain.Investor;
import com.finance.investment.micro.domain.enumeration.Gender;
import com.finance.investment.micro.repository.InvestorRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link InvestorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvestorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/investors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvestorMockMvc;

    private Investor investor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Investor createEntity(EntityManager em) {
        Investor investor = new Investor()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .createdOn(DEFAULT_CREATED_ON);
        return investor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Investor createUpdatedEntity(EntityManager em) {
        Investor investor = new Investor()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .createdOn(UPDATED_CREATED_ON);
        return investor;
    }

    @BeforeEach
    public void initTest() {
        investor = createEntity(em);
    }

    @Test
    @Transactional
    void createInvestor() throws Exception {
        int databaseSizeBeforeCreate = investorRepository.findAll().size();
        // Create the Investor
        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isCreated());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeCreate + 1);
        Investor testInvestor = investorList.get(investorList.size() - 1);
        assertThat(testInvestor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvestor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvestor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInvestor.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testInvestor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testInvestor.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testInvestor.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testInvestor.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testInvestor.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testInvestor.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    void createInvestorWithExistingId() throws Exception {
        // Create the Investor with an existing ID
        investor.setId(1L);

        int databaseSizeBeforeCreate = investorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setName(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setEmail(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setGender(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setPhone(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setAddressLine1(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setCity(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = investorRepository.findAll().size();
        // set the field null
        investor.setCountry(null);

        // Create the Investor, which fails.

        restInvestorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvestors() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList
        restInvestorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @Test
    @Transactional
    void getInvestor() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get the investor
        restInvestorMockMvc
            .perform(get(ENTITY_API_URL_ID, investor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(investor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInvestor() throws Exception {
        // Get the investor
        restInvestorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvestor() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        int databaseSizeBeforeUpdate = investorRepository.findAll().size();

        // Update the investor
        Investor updatedInvestor = investorRepository.findById(investor.getId()).get();
        // Disconnect from session so that the updates on updatedInvestor are not directly saved in db
        em.detach(updatedInvestor);
        updatedInvestor
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .createdOn(UPDATED_CREATED_ON);

        restInvestorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvestor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvestor))
            )
            .andExpect(status().isOk());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
        Investor testInvestor = investorList.get(investorList.size() - 1);
        assertThat(testInvestor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvestor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvestor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInvestor.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInvestor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testInvestor.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testInvestor.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testInvestor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testInvestor.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testInvestor.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();
        investor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();
        investor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(investor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();
        investor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvestorWithPatch() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        int databaseSizeBeforeUpdate = investorRepository.findAll().size();

        // Update the investor using partial update
        Investor partialUpdatedInvestor = new Investor();
        partialUpdatedInvestor.setId(investor.getId());

        partialUpdatedInvestor
            .gender(UPDATED_GENDER)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .createdOn(UPDATED_CREATED_ON);

        restInvestorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestor))
            )
            .andExpect(status().isOk());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
        Investor testInvestor = investorList.get(investorList.size() - 1);
        assertThat(testInvestor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvestor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvestor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInvestor.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInvestor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testInvestor.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testInvestor.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testInvestor.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testInvestor.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testInvestor.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateInvestorWithPatch() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        int databaseSizeBeforeUpdate = investorRepository.findAll().size();

        // Update the investor using partial update
        Investor partialUpdatedInvestor = new Investor();
        partialUpdatedInvestor.setId(investor.getId());

        partialUpdatedInvestor
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .createdOn(UPDATED_CREATED_ON);

        restInvestorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvestor))
            )
            .andExpect(status().isOk());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
        Investor testInvestor = investorList.get(investorList.size() - 1);
        assertThat(testInvestor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvestor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvestor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInvestor.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInvestor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testInvestor.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testInvestor.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testInvestor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testInvestor.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testInvestor.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();
        investor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, investor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();
        investor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(investor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();
        investor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvestor() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        int databaseSizeBeforeDelete = investorRepository.findAll().size();

        // Delete the investor
        restInvestorMockMvc
            .perform(delete(ENTITY_API_URL_ID, investor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
