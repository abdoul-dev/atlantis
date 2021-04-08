package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.Fournisseur;
import com.atlantis.bf.domain.EntreeStock;
import com.atlantis.bf.repository.FournisseurRepository;
import com.atlantis.bf.service.FournisseurService;
import com.atlantis.bf.service.dto.FournisseurDTO;
import com.atlantis.bf.service.mapper.FournisseurMapper;
import com.atlantis.bf.service.dto.FournisseurCriteria;
import com.atlantis.bf.service.FournisseurQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FournisseurResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurMapper fournisseurMapper;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    private FournisseurQueryService fournisseurQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFournisseurMockMvc;

    private Fournisseur fournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .fullName(DEFAULT_FULL_NAME)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE);
        return fournisseur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createUpdatedEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .fullName(UPDATED_FULL_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE);
        return fournisseur;
    }

    @BeforeEach
    public void initTest() {
        fournisseur = createEntity(em);
    }

    @Test
    @Transactional
    public void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();
        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);
        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testFournisseur.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFournisseur.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createFournisseurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setFullName(null);

        // Create the Fournisseur, which fails.
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);


        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFournisseurs() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    
    @Test
    @Transactional
    public void getFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc.perform(get("/api/fournisseurs/{id}", fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }


    @Test
    @Transactional
    public void getFournisseursByIdFiltering() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        Long id = fournisseur.getId();

        defaultFournisseurShouldBeFound("id.equals=" + id);
        defaultFournisseurShouldNotBeFound("id.notEquals=" + id);

        defaultFournisseurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFournisseurShouldNotBeFound("id.greaterThan=" + id);

        defaultFournisseurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFournisseurShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFournisseursByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where fullName equals to DEFAULT_FULL_NAME
        defaultFournisseurShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the fournisseurList where fullName equals to UPDATED_FULL_NAME
        defaultFournisseurShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllFournisseursByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where fullName not equals to DEFAULT_FULL_NAME
        defaultFournisseurShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the fournisseurList where fullName not equals to UPDATED_FULL_NAME
        defaultFournisseurShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllFournisseursByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultFournisseurShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the fournisseurList where fullName equals to UPDATED_FULL_NAME
        defaultFournisseurShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllFournisseursByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where fullName is not null
        defaultFournisseurShouldBeFound("fullName.specified=true");

        // Get all the fournisseurList where fullName is null
        defaultFournisseurShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByFullNameContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where fullName contains DEFAULT_FULL_NAME
        defaultFournisseurShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the fournisseurList where fullName contains UPDATED_FULL_NAME
        defaultFournisseurShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllFournisseursByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where fullName does not contain DEFAULT_FULL_NAME
        defaultFournisseurShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the fournisseurList where fullName does not contain UPDATED_FULL_NAME
        defaultFournisseurShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllFournisseursByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where address equals to DEFAULT_ADDRESS
        defaultFournisseurShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the fournisseurList where address equals to UPDATED_ADDRESS
        defaultFournisseurShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where address not equals to DEFAULT_ADDRESS
        defaultFournisseurShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the fournisseurList where address not equals to UPDATED_ADDRESS
        defaultFournisseurShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultFournisseurShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the fournisseurList where address equals to UPDATED_ADDRESS
        defaultFournisseurShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where address is not null
        defaultFournisseurShouldBeFound("address.specified=true");

        // Get all the fournisseurList where address is null
        defaultFournisseurShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByAddressContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where address contains DEFAULT_ADDRESS
        defaultFournisseurShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the fournisseurList where address contains UPDATED_ADDRESS
        defaultFournisseurShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where address does not contain DEFAULT_ADDRESS
        defaultFournisseurShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the fournisseurList where address does not contain UPDATED_ADDRESS
        defaultFournisseurShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllFournisseursByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where phone equals to DEFAULT_PHONE
        defaultFournisseurShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the fournisseurList where phone equals to UPDATED_PHONE
        defaultFournisseurShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where phone not equals to DEFAULT_PHONE
        defaultFournisseurShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the fournisseurList where phone not equals to UPDATED_PHONE
        defaultFournisseurShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultFournisseurShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the fournisseurList where phone equals to UPDATED_PHONE
        defaultFournisseurShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where phone is not null
        defaultFournisseurShouldBeFound("phone.specified=true");

        // Get all the fournisseurList where phone is null
        defaultFournisseurShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByPhoneContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where phone contains DEFAULT_PHONE
        defaultFournisseurShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the fournisseurList where phone contains UPDATED_PHONE
        defaultFournisseurShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where phone does not contain DEFAULT_PHONE
        defaultFournisseurShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the fournisseurList where phone does not contain UPDATED_PHONE
        defaultFournisseurShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllFournisseursByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);
        EntreeStock stock = EntreeStockResourceIT.createEntity(em);
        em.persist(stock);
        em.flush();
        fournisseur.addStock(stock);
        fournisseurRepository.saveAndFlush(fournisseur);
        Long stockId = stock.getId();

        // Get all the fournisseurList where stock equals to stockId
        defaultFournisseurShouldBeFound("stockId.equals=" + stockId);

        // Get all the fournisseurList where stock equals to stockId + 1
        defaultFournisseurShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFournisseurShouldBeFound(String filter) throws Exception {
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restFournisseurMockMvc.perform(get("/api/fournisseurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFournisseurShouldNotBeFound(String filter) throws Exception {
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFournisseurMockMvc.perform(get("/api/fournisseurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get("/api/fournisseurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).get();
        // Disconnect from session so that the updates on updatedFournisseur are not directly saved in db
        em.detach(updatedFournisseur);
        updatedFournisseur
            .fullName(UPDATED_FULL_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE);
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(updatedFournisseur);

        restFournisseurMockMvc.perform(put("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testFournisseur.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFournisseur.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Create the Fournisseur
        FournisseurDTO fournisseurDTO = fournisseurMapper.toDto(fournisseur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc.perform(put("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        int databaseSizeBeforeDelete = fournisseurRepository.findAll().size();

        // Delete the fournisseur
        restFournisseurMockMvc.perform(delete("/api/fournisseurs/{id}", fournisseur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
