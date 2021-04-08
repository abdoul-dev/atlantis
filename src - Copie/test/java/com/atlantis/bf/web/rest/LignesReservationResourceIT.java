package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.LignesReservation;
import com.atlantis.bf.domain.Products;
import com.atlantis.bf.repository.LignesReservationRepository;
import com.atlantis.bf.service.LignesReservationService;
import com.atlantis.bf.service.dto.LignesReservationDTO;
import com.atlantis.bf.service.mapper.LignesReservationMapper;
import com.atlantis.bf.service.dto.LignesReservationCriteria;
import com.atlantis.bf.service.LignesReservationQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LignesReservationResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LignesReservationResourceIT {

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITE = new BigDecimal(1 - 1);

    @Autowired
    private LignesReservationRepository lignesReservationRepository;

    @Autowired
    private LignesReservationMapper lignesReservationMapper;

    @Autowired
    private LignesReservationService lignesReservationService;

    @Autowired
    private LignesReservationQueryService lignesReservationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLignesReservationMockMvc;

    private LignesReservation lignesReservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LignesReservation createEntity(EntityManager em) {
        LignesReservation lignesReservation = new LignesReservation()
            .quantite(DEFAULT_QUANTITE);
        return lignesReservation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LignesReservation createUpdatedEntity(EntityManager em) {
        LignesReservation lignesReservation = new LignesReservation()
            .quantite(UPDATED_QUANTITE);
        return lignesReservation;
    }

    @BeforeEach
    public void initTest() {
        lignesReservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createLignesReservation() throws Exception {
        int databaseSizeBeforeCreate = lignesReservationRepository.findAll().size();
        // Create the LignesReservation
        LignesReservationDTO lignesReservationDTO = lignesReservationMapper.toDto(lignesReservation);
        restLignesReservationMockMvc.perform(post("/api/lignes-reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesReservationDTO)))
            .andExpect(status().isCreated());

        // Validate the LignesReservation in the database
        List<LignesReservation> lignesReservationList = lignesReservationRepository.findAll();
        assertThat(lignesReservationList).hasSize(databaseSizeBeforeCreate + 1);
        LignesReservation testLignesReservation = lignesReservationList.get(lignesReservationList.size() - 1);
        assertThat(testLignesReservation.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    public void createLignesReservationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lignesReservationRepository.findAll().size();

        // Create the LignesReservation with an existing ID
        lignesReservation.setId(1L);
        LignesReservationDTO lignesReservationDTO = lignesReservationMapper.toDto(lignesReservation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLignesReservationMockMvc.perform(post("/api/lignes-reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesReservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LignesReservation in the database
        List<LignesReservation> lignesReservationList = lignesReservationRepository.findAll();
        assertThat(lignesReservationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignesReservationRepository.findAll().size();
        // set the field null
        lignesReservation.setQuantite(null);

        // Create the LignesReservation, which fails.
        LignesReservationDTO lignesReservationDTO = lignesReservationMapper.toDto(lignesReservation);


        restLignesReservationMockMvc.perform(post("/api/lignes-reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesReservationDTO)))
            .andExpect(status().isBadRequest());

        List<LignesReservation> lignesReservationList = lignesReservationRepository.findAll();
        assertThat(lignesReservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLignesReservations() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignesReservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())));
    }
    
    @Test
    @Transactional
    public void getLignesReservation() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get the lignesReservation
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations/{id}", lignesReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lignesReservation.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()));
    }


    @Test
    @Transactional
    public void getLignesReservationsByIdFiltering() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        Long id = lignesReservation.getId();

        defaultLignesReservationShouldBeFound("id.equals=" + id);
        defaultLignesReservationShouldNotBeFound("id.notEquals=" + id);

        defaultLignesReservationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLignesReservationShouldNotBeFound("id.greaterThan=" + id);

        defaultLignesReservationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLignesReservationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite equals to DEFAULT_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.equals=" + DEFAULT_QUANTITE);

        // Get all the lignesReservationList where quantite equals to UPDATED_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.equals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite not equals to DEFAULT_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.notEquals=" + DEFAULT_QUANTITE);

        // Get all the lignesReservationList where quantite not equals to UPDATED_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.notEquals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsInShouldWork() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite in DEFAULT_QUANTITE or UPDATED_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.in=" + DEFAULT_QUANTITE + "," + UPDATED_QUANTITE);

        // Get all the lignesReservationList where quantite equals to UPDATED_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.in=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite is not null
        defaultLignesReservationShouldBeFound("quantite.specified=true");

        // Get all the lignesReservationList where quantite is null
        defaultLignesReservationShouldNotBeFound("quantite.specified=false");
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite is greater than or equal to DEFAULT_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.greaterThanOrEqual=" + DEFAULT_QUANTITE);

        // Get all the lignesReservationList where quantite is greater than or equal to UPDATED_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.greaterThanOrEqual=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite is less than or equal to DEFAULT_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.lessThanOrEqual=" + DEFAULT_QUANTITE);

        // Get all the lignesReservationList where quantite is less than or equal to SMALLER_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.lessThanOrEqual=" + SMALLER_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsLessThanSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite is less than DEFAULT_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.lessThan=" + DEFAULT_QUANTITE);

        // Get all the lignesReservationList where quantite is less than UPDATED_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.lessThan=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesReservationsByQuantiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        // Get all the lignesReservationList where quantite is greater than DEFAULT_QUANTITE
        defaultLignesReservationShouldNotBeFound("quantite.greaterThan=" + DEFAULT_QUANTITE);

        // Get all the lignesReservationList where quantite is greater than SMALLER_QUANTITE
        defaultLignesReservationShouldBeFound("quantite.greaterThan=" + SMALLER_QUANTITE);
    }


    @Test
    @Transactional
    public void getAllLignesReservationsByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);
        Products products = ProductsResourceIT.createEntity(em);
        em.persist(products);
        em.flush();
        lignesReservation.setProducts(products);
        lignesReservationRepository.saveAndFlush(lignesReservation);
        Long productsId = products.getId();

        // Get all the lignesReservationList where products equals to productsId
        defaultLignesReservationShouldBeFound("productsId.equals=" + productsId);

        // Get all the lignesReservationList where products equals to productsId + 1
        defaultLignesReservationShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLignesReservationShouldBeFound(String filter) throws Exception {
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignesReservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())));

        // Check, that the count call also returns 1
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLignesReservationShouldNotBeFound(String filter) throws Exception {
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLignesReservation() throws Exception {
        // Get the lignesReservation
        restLignesReservationMockMvc.perform(get("/api/lignes-reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLignesReservation() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        int databaseSizeBeforeUpdate = lignesReservationRepository.findAll().size();

        // Update the lignesReservation
        LignesReservation updatedLignesReservation = lignesReservationRepository.findById(lignesReservation.getId()).get();
        // Disconnect from session so that the updates on updatedLignesReservation are not directly saved in db
        em.detach(updatedLignesReservation);
        updatedLignesReservation
            .quantite(UPDATED_QUANTITE);
        LignesReservationDTO lignesReservationDTO = lignesReservationMapper.toDto(updatedLignesReservation);

        restLignesReservationMockMvc.perform(put("/api/lignes-reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesReservationDTO)))
            .andExpect(status().isOk());

        // Validate the LignesReservation in the database
        List<LignesReservation> lignesReservationList = lignesReservationRepository.findAll();
        assertThat(lignesReservationList).hasSize(databaseSizeBeforeUpdate);
        LignesReservation testLignesReservation = lignesReservationList.get(lignesReservationList.size() - 1);
        assertThat(testLignesReservation.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void updateNonExistingLignesReservation() throws Exception {
        int databaseSizeBeforeUpdate = lignesReservationRepository.findAll().size();

        // Create the LignesReservation
        LignesReservationDTO lignesReservationDTO = lignesReservationMapper.toDto(lignesReservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLignesReservationMockMvc.perform(put("/api/lignes-reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesReservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LignesReservation in the database
        List<LignesReservation> lignesReservationList = lignesReservationRepository.findAll();
        assertThat(lignesReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLignesReservation() throws Exception {
        // Initialize the database
        lignesReservationRepository.saveAndFlush(lignesReservation);

        int databaseSizeBeforeDelete = lignesReservationRepository.findAll().size();

        // Delete the lignesReservation
        restLignesReservationMockMvc.perform(delete("/api/lignes-reservations/{id}", lignesReservation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LignesReservation> lignesReservationList = lignesReservationRepository.findAll();
        assertThat(lignesReservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
