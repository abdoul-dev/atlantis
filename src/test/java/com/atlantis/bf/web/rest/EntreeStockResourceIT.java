package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.EntreeStock;
import com.atlantis.bf.domain.LigneEntreeStock;
import com.atlantis.bf.domain.Fournisseur;
import com.atlantis.bf.repository.EntreeStockRepository;
import com.atlantis.bf.service.EntreeStockService;
import com.atlantis.bf.service.dto.EntreeStockDTO;
import com.atlantis.bf.service.mapper.EntreeStockMapper;
import com.atlantis.bf.service.dto.EntreeStockCriteria;
import com.atlantis.bf.service.EntreeStockQueryService;

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
 * Integration tests for the {@link EntreeStockResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EntreeStockResourceIT {

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_ANNULE = false;
    private static final Boolean UPDATED_ANNULE = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private EntreeStockRepository entreeStockRepository;

    @Autowired
    private EntreeStockMapper entreeStockMapper;

    @Autowired
    private EntreeStockService entreeStockService;

    @Autowired
    private EntreeStockQueryService entreeStockQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntreeStockMockMvc;

    private EntreeStock entreeStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntreeStock createEntity(EntityManager em) {
        EntreeStock entreeStock = new EntreeStock()
            .montant(DEFAULT_MONTANT)
            .annule(DEFAULT_ANNULE)
            .date(DEFAULT_DATE);
        return entreeStock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntreeStock createUpdatedEntity(EntityManager em) {
        EntreeStock entreeStock = new EntreeStock()
            .montant(UPDATED_MONTANT)
            .annule(UPDATED_ANNULE)
            .date(UPDATED_DATE);
        return entreeStock;
    }

    @BeforeEach
    public void initTest() {
        entreeStock = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntreeStock() throws Exception {
        int databaseSizeBeforeCreate = entreeStockRepository.findAll().size();
        // Create the EntreeStock
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(entreeStock);
        restEntreeStockMockMvc.perform(post("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isCreated());

        // Validate the EntreeStock in the database
        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeCreate + 1);
        EntreeStock testEntreeStock = entreeStockList.get(entreeStockList.size() - 1);
        assertThat(testEntreeStock.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testEntreeStock.isAnnule()).isEqualTo(DEFAULT_ANNULE);
        assertThat(testEntreeStock.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createEntreeStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entreeStockRepository.findAll().size();

        // Create the EntreeStock with an existing ID
        entreeStock.setId(1L);
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(entreeStock);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntreeStockMockMvc.perform(post("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EntreeStock in the database
        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeStockRepository.findAll().size();
        // set the field null
        entreeStock.setMontant(null);

        // Create the EntreeStock, which fails.
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(entreeStock);


        restEntreeStockMockMvc.perform(post("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isBadRequest());

        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnuleIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeStockRepository.findAll().size();
        // set the field null
        entreeStock.setAnnule(null);

        // Create the EntreeStock, which fails.
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(entreeStock);


        restEntreeStockMockMvc.perform(post("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isBadRequest());

        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = entreeStockRepository.findAll().size();
        // set the field null
        entreeStock.setDate(null);

        // Create the EntreeStock, which fails.
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(entreeStock);


        restEntreeStockMockMvc.perform(post("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isBadRequest());

        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntreeStocks() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList
        restEntreeStockMockMvc.perform(get("/api/entree-stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreeStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getEntreeStock() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get the entreeStock
        restEntreeStockMockMvc.perform(get("/api/entree-stocks/{id}", entreeStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entreeStock.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.annule").value(DEFAULT_ANNULE.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }


    @Test
    @Transactional
    public void getEntreeStocksByIdFiltering() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        Long id = entreeStock.getId();

        defaultEntreeStockShouldBeFound("id.equals=" + id);
        defaultEntreeStockShouldNotBeFound("id.notEquals=" + id);

        defaultEntreeStockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEntreeStockShouldNotBeFound("id.greaterThan=" + id);

        defaultEntreeStockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEntreeStockShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant equals to DEFAULT_MONTANT
        defaultEntreeStockShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the entreeStockList where montant equals to UPDATED_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant not equals to DEFAULT_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.notEquals=" + DEFAULT_MONTANT);

        // Get all the entreeStockList where montant not equals to UPDATED_MONTANT
        defaultEntreeStockShouldBeFound("montant.notEquals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultEntreeStockShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the entreeStockList where montant equals to UPDATED_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant is not null
        defaultEntreeStockShouldBeFound("montant.specified=true");

        // Get all the entreeStockList where montant is null
        defaultEntreeStockShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant is greater than or equal to DEFAULT_MONTANT
        defaultEntreeStockShouldBeFound("montant.greaterThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the entreeStockList where montant is greater than or equal to UPDATED_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant is less than or equal to DEFAULT_MONTANT
        defaultEntreeStockShouldBeFound("montant.lessThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the entreeStockList where montant is less than or equal to SMALLER_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant is less than DEFAULT_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.lessThan=" + DEFAULT_MONTANT);

        // Get all the entreeStockList where montant is less than UPDATED_MONTANT
        defaultEntreeStockShouldBeFound("montant.lessThan=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where montant is greater than DEFAULT_MONTANT
        defaultEntreeStockShouldNotBeFound("montant.greaterThan=" + DEFAULT_MONTANT);

        // Get all the entreeStockList where montant is greater than SMALLER_MONTANT
        defaultEntreeStockShouldBeFound("montant.greaterThan=" + SMALLER_MONTANT);
    }


    @Test
    @Transactional
    public void getAllEntreeStocksByAnnuleIsEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where annule equals to DEFAULT_ANNULE
        defaultEntreeStockShouldBeFound("annule.equals=" + DEFAULT_ANNULE);

        // Get all the entreeStockList where annule equals to UPDATED_ANNULE
        defaultEntreeStockShouldNotBeFound("annule.equals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByAnnuleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where annule not equals to DEFAULT_ANNULE
        defaultEntreeStockShouldNotBeFound("annule.notEquals=" + DEFAULT_ANNULE);

        // Get all the entreeStockList where annule not equals to UPDATED_ANNULE
        defaultEntreeStockShouldBeFound("annule.notEquals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByAnnuleIsInShouldWork() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where annule in DEFAULT_ANNULE or UPDATED_ANNULE
        defaultEntreeStockShouldBeFound("annule.in=" + DEFAULT_ANNULE + "," + UPDATED_ANNULE);

        // Get all the entreeStockList where annule equals to UPDATED_ANNULE
        defaultEntreeStockShouldNotBeFound("annule.in=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByAnnuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where annule is not null
        defaultEntreeStockShouldBeFound("annule.specified=true");

        // Get all the entreeStockList where annule is null
        defaultEntreeStockShouldNotBeFound("annule.specified=false");
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date equals to DEFAULT_DATE
        defaultEntreeStockShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the entreeStockList where date equals to UPDATED_DATE
        defaultEntreeStockShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date not equals to DEFAULT_DATE
        defaultEntreeStockShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the entreeStockList where date not equals to UPDATED_DATE
        defaultEntreeStockShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsInShouldWork() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date in DEFAULT_DATE or UPDATED_DATE
        defaultEntreeStockShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the entreeStockList where date equals to UPDATED_DATE
        defaultEntreeStockShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date is not null
        defaultEntreeStockShouldBeFound("date.specified=true");

        // Get all the entreeStockList where date is null
        defaultEntreeStockShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date is greater than or equal to DEFAULT_DATE
        defaultEntreeStockShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the entreeStockList where date is greater than or equal to UPDATED_DATE
        defaultEntreeStockShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date is less than or equal to DEFAULT_DATE
        defaultEntreeStockShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the entreeStockList where date is less than or equal to SMALLER_DATE
        defaultEntreeStockShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date is less than DEFAULT_DATE
        defaultEntreeStockShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the entreeStockList where date is less than UPDATED_DATE
        defaultEntreeStockShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEntreeStocksByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        // Get all the entreeStockList where date is greater than DEFAULT_DATE
        defaultEntreeStockShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the entreeStockList where date is greater than SMALLER_DATE
        defaultEntreeStockShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllEntreeStocksByLigneEntreeStockIsEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);
        LigneEntreeStock ligneEntreeStock = LigneEntreeStockResourceIT.createEntity(em);
        em.persist(ligneEntreeStock);
        em.flush();
        entreeStock.addLigneEntreeStock(ligneEntreeStock);
        entreeStockRepository.saveAndFlush(entreeStock);
        Long ligneEntreeStockId = ligneEntreeStock.getId();

        // Get all the entreeStockList where ligneEntreeStock equals to ligneEntreeStockId
        defaultEntreeStockShouldBeFound("ligneEntreeStockId.equals=" + ligneEntreeStockId);

        // Get all the entreeStockList where ligneEntreeStock equals to ligneEntreeStockId + 1
        defaultEntreeStockShouldNotBeFound("ligneEntreeStockId.equals=" + (ligneEntreeStockId + 1));
    }


    @Test
    @Transactional
    public void getAllEntreeStocksByFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);
        Fournisseur fournisseur = FournisseurResourceIT.createEntity(em);
        em.persist(fournisseur);
        em.flush();
        entreeStock.setFournisseur(fournisseur);
        entreeStockRepository.saveAndFlush(entreeStock);
        Long fournisseurId = fournisseur.getId();

        // Get all the entreeStockList where fournisseur equals to fournisseurId
        defaultEntreeStockShouldBeFound("fournisseurId.equals=" + fournisseurId);

        // Get all the entreeStockList where fournisseur equals to fournisseurId + 1
        defaultEntreeStockShouldNotBeFound("fournisseurId.equals=" + (fournisseurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntreeStockShouldBeFound(String filter) throws Exception {
        restEntreeStockMockMvc.perform(get("/api/entree-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreeStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restEntreeStockMockMvc.perform(get("/api/entree-stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntreeStockShouldNotBeFound(String filter) throws Exception {
        restEntreeStockMockMvc.perform(get("/api/entree-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntreeStockMockMvc.perform(get("/api/entree-stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEntreeStock() throws Exception {
        // Get the entreeStock
        restEntreeStockMockMvc.perform(get("/api/entree-stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntreeStock() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        int databaseSizeBeforeUpdate = entreeStockRepository.findAll().size();

        // Update the entreeStock
        EntreeStock updatedEntreeStock = entreeStockRepository.findById(entreeStock.getId()).get();
        // Disconnect from session so that the updates on updatedEntreeStock are not directly saved in db
        em.detach(updatedEntreeStock);
        updatedEntreeStock
            .montant(UPDATED_MONTANT)
            .annule(UPDATED_ANNULE)
            .date(UPDATED_DATE);
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(updatedEntreeStock);

        restEntreeStockMockMvc.perform(put("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isOk());

        // Validate the EntreeStock in the database
        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeUpdate);
        EntreeStock testEntreeStock = entreeStockList.get(entreeStockList.size() - 1);
        assertThat(testEntreeStock.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testEntreeStock.isAnnule()).isEqualTo(UPDATED_ANNULE);
        assertThat(testEntreeStock.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEntreeStock() throws Exception {
        int databaseSizeBeforeUpdate = entreeStockRepository.findAll().size();

        // Create the EntreeStock
        EntreeStockDTO entreeStockDTO = entreeStockMapper.toDto(entreeStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntreeStockMockMvc.perform(put("/api/entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(entreeStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EntreeStock in the database
        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntreeStock() throws Exception {
        // Initialize the database
        entreeStockRepository.saveAndFlush(entreeStock);

        int databaseSizeBeforeDelete = entreeStockRepository.findAll().size();

        // Delete the entreeStock
        restEntreeStockMockMvc.perform(delete("/api/entree-stocks/{id}", entreeStock.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EntreeStock> entreeStockList = entreeStockRepository.findAll();
        assertThat(entreeStockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
