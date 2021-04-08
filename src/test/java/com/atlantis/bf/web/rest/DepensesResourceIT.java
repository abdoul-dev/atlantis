package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.Depenses;
import com.atlantis.bf.domain.TypeDepense;
import com.atlantis.bf.repository.DepensesRepository;
import com.atlantis.bf.service.DepensesService;
import com.atlantis.bf.service.dto.DepensesDTO;
import com.atlantis.bf.service.mapper.DepensesMapper;
import com.atlantis.bf.service.dto.DepensesCriteria;
import com.atlantis.bf.service.DepensesQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DepensesResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DepensesResourceIT {

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT = new BigDecimal(1 - 1);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ANNULE = false;
    private static final Boolean UPDATED_ANNULE = true;

    @Autowired
    private DepensesRepository depensesRepository;

    @Autowired
    private DepensesMapper depensesMapper;

    @Autowired
    private DepensesService depensesService;

    @Autowired
    private DepensesQueryService depensesQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepensesMockMvc;

    private Depenses depenses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depenses createEntity(EntityManager em) {
        Depenses depenses = new Depenses()
            .montant(DEFAULT_MONTANT)
            .comments(DEFAULT_COMMENTS)
            .date(DEFAULT_DATE)
            .annule(DEFAULT_ANNULE);
        return depenses;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depenses createUpdatedEntity(EntityManager em) {
        Depenses depenses = new Depenses()
            .montant(UPDATED_MONTANT)
            .comments(UPDATED_COMMENTS)
            .date(UPDATED_DATE)
            .annule(UPDATED_ANNULE);
        return depenses;
    }

    @BeforeEach
    public void initTest() {
        depenses = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepenses() throws Exception {
        int databaseSizeBeforeCreate = depensesRepository.findAll().size();
        // Create the Depenses
        DepensesDTO depensesDTO = depensesMapper.toDto(depenses);
        restDepensesMockMvc.perform(post("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isCreated());

        // Validate the Depenses in the database
        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeCreate + 1);
        Depenses testDepenses = depensesList.get(depensesList.size() - 1);
        assertThat(testDepenses.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDepenses.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testDepenses.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDepenses.isAnnule()).isEqualTo(DEFAULT_ANNULE);
    }

    @Test
    @Transactional
    public void createDepensesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depensesRepository.findAll().size();

        // Create the Depenses with an existing ID
        depenses.setId(1L);
        DepensesDTO depensesDTO = depensesMapper.toDto(depenses);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepensesMockMvc.perform(post("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depenses in the database
        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = depensesRepository.findAll().size();
        // set the field null
        depenses.setMontant(null);

        // Create the Depenses, which fails.
        DepensesDTO depensesDTO = depensesMapper.toDto(depenses);


        restDepensesMockMvc.perform(post("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isBadRequest());

        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depensesRepository.findAll().size();
        // set the field null
        depenses.setDate(null);

        // Create the Depenses, which fails.
        DepensesDTO depensesDTO = depensesMapper.toDto(depenses);


        restDepensesMockMvc.perform(post("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isBadRequest());

        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnuleIsRequired() throws Exception {
        int databaseSizeBeforeTest = depensesRepository.findAll().size();
        // set the field null
        depenses.setAnnule(null);

        // Create the Depenses, which fails.
        DepensesDTO depensesDTO = depensesMapper.toDto(depenses);


        restDepensesMockMvc.perform(post("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isBadRequest());

        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepenses() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList
        restDepensesMockMvc.perform(get("/api/depenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDepenses() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get the depenses
        restDepensesMockMvc.perform(get("/api/depenses/{id}", depenses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depenses.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.annule").value(DEFAULT_ANNULE.booleanValue()));
    }


    @Test
    @Transactional
    public void getDepensesByIdFiltering() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        Long id = depenses.getId();

        defaultDepensesShouldBeFound("id.equals=" + id);
        defaultDepensesShouldNotBeFound("id.notEquals=" + id);

        defaultDepensesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepensesShouldNotBeFound("id.greaterThan=" + id);

        defaultDepensesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepensesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDepensesByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant equals to DEFAULT_MONTANT
        defaultDepensesShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the depensesList where montant equals to UPDATED_MONTANT
        defaultDepensesShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant not equals to DEFAULT_MONTANT
        defaultDepensesShouldNotBeFound("montant.notEquals=" + DEFAULT_MONTANT);

        // Get all the depensesList where montant not equals to UPDATED_MONTANT
        defaultDepensesShouldBeFound("montant.notEquals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultDepensesShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the depensesList where montant equals to UPDATED_MONTANT
        defaultDepensesShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant is not null
        defaultDepensesShouldBeFound("montant.specified=true");

        // Get all the depensesList where montant is null
        defaultDepensesShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant is greater than or equal to DEFAULT_MONTANT
        defaultDepensesShouldBeFound("montant.greaterThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the depensesList where montant is greater than or equal to UPDATED_MONTANT
        defaultDepensesShouldNotBeFound("montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant is less than or equal to DEFAULT_MONTANT
        defaultDepensesShouldBeFound("montant.lessThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the depensesList where montant is less than or equal to SMALLER_MONTANT
        defaultDepensesShouldNotBeFound("montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant is less than DEFAULT_MONTANT
        defaultDepensesShouldNotBeFound("montant.lessThan=" + DEFAULT_MONTANT);

        // Get all the depensesList where montant is less than UPDATED_MONTANT
        defaultDepensesShouldBeFound("montant.lessThan=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllDepensesByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where montant is greater than DEFAULT_MONTANT
        defaultDepensesShouldNotBeFound("montant.greaterThan=" + DEFAULT_MONTANT);

        // Get all the depensesList where montant is greater than SMALLER_MONTANT
        defaultDepensesShouldBeFound("montant.greaterThan=" + SMALLER_MONTANT);
    }


    @Test
    @Transactional
    public void getAllDepensesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where comments equals to DEFAULT_COMMENTS
        defaultDepensesShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the depensesList where comments equals to UPDATED_COMMENTS
        defaultDepensesShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllDepensesByCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where comments not equals to DEFAULT_COMMENTS
        defaultDepensesShouldNotBeFound("comments.notEquals=" + DEFAULT_COMMENTS);

        // Get all the depensesList where comments not equals to UPDATED_COMMENTS
        defaultDepensesShouldBeFound("comments.notEquals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllDepensesByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultDepensesShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the depensesList where comments equals to UPDATED_COMMENTS
        defaultDepensesShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllDepensesByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where comments is not null
        defaultDepensesShouldBeFound("comments.specified=true");

        // Get all the depensesList where comments is null
        defaultDepensesShouldNotBeFound("comments.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepensesByCommentsContainsSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where comments contains DEFAULT_COMMENTS
        defaultDepensesShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the depensesList where comments contains UPDATED_COMMENTS
        defaultDepensesShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllDepensesByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where comments does not contain DEFAULT_COMMENTS
        defaultDepensesShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the depensesList where comments does not contain UPDATED_COMMENTS
        defaultDepensesShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }


    @Test
    @Transactional
    public void getAllDepensesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date equals to DEFAULT_DATE
        defaultDepensesShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the depensesList where date equals to UPDATED_DATE
        defaultDepensesShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date not equals to DEFAULT_DATE
        defaultDepensesShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the depensesList where date not equals to UPDATED_DATE
        defaultDepensesShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date in DEFAULT_DATE or UPDATED_DATE
        defaultDepensesShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the depensesList where date equals to UPDATED_DATE
        defaultDepensesShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date is not null
        defaultDepensesShouldBeFound("date.specified=true");

        // Get all the depensesList where date is null
        defaultDepensesShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date is greater than or equal to DEFAULT_DATE
        defaultDepensesShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the depensesList where date is greater than or equal to UPDATED_DATE
        defaultDepensesShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date is less than or equal to DEFAULT_DATE
        defaultDepensesShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the depensesList where date is less than or equal to SMALLER_DATE
        defaultDepensesShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date is less than DEFAULT_DATE
        defaultDepensesShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the depensesList where date is less than UPDATED_DATE
        defaultDepensesShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDepensesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where date is greater than DEFAULT_DATE
        defaultDepensesShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the depensesList where date is greater than SMALLER_DATE
        defaultDepensesShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllDepensesByAnnuleIsEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where annule equals to DEFAULT_ANNULE
        defaultDepensesShouldBeFound("annule.equals=" + DEFAULT_ANNULE);

        // Get all the depensesList where annule equals to UPDATED_ANNULE
        defaultDepensesShouldNotBeFound("annule.equals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllDepensesByAnnuleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where annule not equals to DEFAULT_ANNULE
        defaultDepensesShouldNotBeFound("annule.notEquals=" + DEFAULT_ANNULE);

        // Get all the depensesList where annule not equals to UPDATED_ANNULE
        defaultDepensesShouldBeFound("annule.notEquals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllDepensesByAnnuleIsInShouldWork() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where annule in DEFAULT_ANNULE or UPDATED_ANNULE
        defaultDepensesShouldBeFound("annule.in=" + DEFAULT_ANNULE + "," + UPDATED_ANNULE);

        // Get all the depensesList where annule equals to UPDATED_ANNULE
        defaultDepensesShouldNotBeFound("annule.in=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllDepensesByAnnuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        // Get all the depensesList where annule is not null
        defaultDepensesShouldBeFound("annule.specified=true");

        // Get all the depensesList where annule is null
        defaultDepensesShouldNotBeFound("annule.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepensesByTypeDepenseIsEqualToSomething() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);
        TypeDepense typeDepense = TypeDepenseResourceIT.createEntity(em);
        em.persist(typeDepense);
        em.flush();
        depenses.setTypeDepense(typeDepense);
        depensesRepository.saveAndFlush(depenses);
        Long typeDepenseId = typeDepense.getId();

        // Get all the depensesList where typeDepense equals to typeDepenseId
        defaultDepensesShouldBeFound("typeDepenseId.equals=" + typeDepenseId);

        // Get all the depensesList where typeDepense equals to typeDepenseId + 1
        defaultDepensesShouldNotBeFound("typeDepenseId.equals=" + (typeDepenseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepensesShouldBeFound(String filter) throws Exception {
        restDepensesMockMvc.perform(get("/api/depenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depenses.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())));

        // Check, that the count call also returns 1
        restDepensesMockMvc.perform(get("/api/depenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepensesShouldNotBeFound(String filter) throws Exception {
        restDepensesMockMvc.perform(get("/api/depenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepensesMockMvc.perform(get("/api/depenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDepenses() throws Exception {
        // Get the depenses
        restDepensesMockMvc.perform(get("/api/depenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepenses() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        int databaseSizeBeforeUpdate = depensesRepository.findAll().size();

        // Update the depenses
        Depenses updatedDepenses = depensesRepository.findById(depenses.getId()).get();
        // Disconnect from session so that the updates on updatedDepenses are not directly saved in db
        em.detach(updatedDepenses);
        updatedDepenses
            .montant(UPDATED_MONTANT)
            .comments(UPDATED_COMMENTS)
            .date(UPDATED_DATE)
            .annule(UPDATED_ANNULE);
        DepensesDTO depensesDTO = depensesMapper.toDto(updatedDepenses);

        restDepensesMockMvc.perform(put("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isOk());

        // Validate the Depenses in the database
        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeUpdate);
        Depenses testDepenses = depensesList.get(depensesList.size() - 1);
        assertThat(testDepenses.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDepenses.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testDepenses.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDepenses.isAnnule()).isEqualTo(UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void updateNonExistingDepenses() throws Exception {
        int databaseSizeBeforeUpdate = depensesRepository.findAll().size();

        // Create the Depenses
        DepensesDTO depensesDTO = depensesMapper.toDto(depenses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepensesMockMvc.perform(put("/api/depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(depensesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depenses in the database
        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDepenses() throws Exception {
        // Initialize the database
        depensesRepository.saveAndFlush(depenses);

        int databaseSizeBeforeDelete = depensesRepository.findAll().size();

        // Delete the depenses
        restDepensesMockMvc.perform(delete("/api/depenses/{id}", depenses.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Depenses> depensesList = depensesRepository.findAll();
        assertThat(depensesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
