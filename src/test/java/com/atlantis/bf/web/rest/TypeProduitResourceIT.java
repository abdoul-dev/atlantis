package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.TypeProduit;
import com.atlantis.bf.domain.Products;
import com.atlantis.bf.repository.TypeProduitRepository;
import com.atlantis.bf.service.TypeProduitService;
import com.atlantis.bf.service.dto.TypeProduitDTO;
import com.atlantis.bf.service.mapper.TypeProduitMapper;
import com.atlantis.bf.service.dto.TypeProduitCriteria;
import com.atlantis.bf.service.TypeProduitQueryService;

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
 * Integration tests for the {@link TypeProduitResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TypeProduitResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DISABLED = false;
    private static final Boolean UPDATED_IS_DISABLED = true;

    @Autowired
    private TypeProduitRepository typeProduitRepository;

    @Autowired
    private TypeProduitMapper typeProduitMapper;

    @Autowired
    private TypeProduitService typeProduitService;

    @Autowired
    private TypeProduitQueryService typeProduitQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeProduitMockMvc;

    private TypeProduit typeProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeProduit createEntity(EntityManager em) {
        TypeProduit typeProduit = new TypeProduit()
            .libelle(DEFAULT_LIBELLE)
            .isDisabled(DEFAULT_IS_DISABLED);
        return typeProduit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeProduit createUpdatedEntity(EntityManager em) {
        TypeProduit typeProduit = new TypeProduit()
            .libelle(UPDATED_LIBELLE)
            .isDisabled(UPDATED_IS_DISABLED);
        return typeProduit;
    }

    @BeforeEach
    public void initTest() {
        typeProduit = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeProduit() throws Exception {
        int databaseSizeBeforeCreate = typeProduitRepository.findAll().size();
        // Create the TypeProduit
        TypeProduitDTO typeProduitDTO = typeProduitMapper.toDto(typeProduit);
        restTypeProduitMockMvc.perform(post("/api/type-produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeProduitDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeProduit in the database
        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeCreate + 1);
        TypeProduit testTypeProduit = typeProduitList.get(typeProduitList.size() - 1);
        assertThat(testTypeProduit.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeProduit.isIsDisabled()).isEqualTo(DEFAULT_IS_DISABLED);
    }

    @Test
    @Transactional
    public void createTypeProduitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeProduitRepository.findAll().size();

        // Create the TypeProduit with an existing ID
        typeProduit.setId(1L);
        TypeProduitDTO typeProduitDTO = typeProduitMapper.toDto(typeProduit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeProduitMockMvc.perform(post("/api/type-produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeProduitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeProduit in the database
        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeProduitRepository.findAll().size();
        // set the field null
        typeProduit.setLibelle(null);

        // Create the TypeProduit, which fails.
        TypeProduitDTO typeProduitDTO = typeProduitMapper.toDto(typeProduit);


        restTypeProduitMockMvc.perform(post("/api/type-produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeProduitDTO)))
            .andExpect(status().isBadRequest());

        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDisabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeProduitRepository.findAll().size();
        // set the field null
        typeProduit.setIsDisabled(null);

        // Create the TypeProduit, which fails.
        TypeProduitDTO typeProduitDTO = typeProduitMapper.toDto(typeProduit);


        restTypeProduitMockMvc.perform(post("/api/type-produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeProduitDTO)))
            .andExpect(status().isBadRequest());

        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeProduits() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList
        restTypeProduitMockMvc.perform(get("/api/type-produits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTypeProduit() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get the typeProduit
        restTypeProduitMockMvc.perform(get("/api/type-produits/{id}", typeProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeProduit.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.isDisabled").value(DEFAULT_IS_DISABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getTypeProduitsByIdFiltering() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        Long id = typeProduit.getId();

        defaultTypeProduitShouldBeFound("id.equals=" + id);
        defaultTypeProduitShouldNotBeFound("id.notEquals=" + id);

        defaultTypeProduitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeProduitShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeProduitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeProduitShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTypeProduitsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where libelle equals to DEFAULT_LIBELLE
        defaultTypeProduitShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the typeProduitList where libelle equals to UPDATED_LIBELLE
        defaultTypeProduitShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where libelle not equals to DEFAULT_LIBELLE
        defaultTypeProduitShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the typeProduitList where libelle not equals to UPDATED_LIBELLE
        defaultTypeProduitShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultTypeProduitShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the typeProduitList where libelle equals to UPDATED_LIBELLE
        defaultTypeProduitShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where libelle is not null
        defaultTypeProduitShouldBeFound("libelle.specified=true");

        // Get all the typeProduitList where libelle is null
        defaultTypeProduitShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllTypeProduitsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where libelle contains DEFAULT_LIBELLE
        defaultTypeProduitShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the typeProduitList where libelle contains UPDATED_LIBELLE
        defaultTypeProduitShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where libelle does not contain DEFAULT_LIBELLE
        defaultTypeProduitShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the typeProduitList where libelle does not contain UPDATED_LIBELLE
        defaultTypeProduitShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllTypeProduitsByIsDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where isDisabled equals to DEFAULT_IS_DISABLED
        defaultTypeProduitShouldBeFound("isDisabled.equals=" + DEFAULT_IS_DISABLED);

        // Get all the typeProduitList where isDisabled equals to UPDATED_IS_DISABLED
        defaultTypeProduitShouldNotBeFound("isDisabled.equals=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByIsDisabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where isDisabled not equals to DEFAULT_IS_DISABLED
        defaultTypeProduitShouldNotBeFound("isDisabled.notEquals=" + DEFAULT_IS_DISABLED);

        // Get all the typeProduitList where isDisabled not equals to UPDATED_IS_DISABLED
        defaultTypeProduitShouldBeFound("isDisabled.notEquals=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByIsDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where isDisabled in DEFAULT_IS_DISABLED or UPDATED_IS_DISABLED
        defaultTypeProduitShouldBeFound("isDisabled.in=" + DEFAULT_IS_DISABLED + "," + UPDATED_IS_DISABLED);

        // Get all the typeProduitList where isDisabled equals to UPDATED_IS_DISABLED
        defaultTypeProduitShouldNotBeFound("isDisabled.in=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByIsDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        // Get all the typeProduitList where isDisabled is not null
        defaultTypeProduitShouldBeFound("isDisabled.specified=true");

        // Get all the typeProduitList where isDisabled is null
        defaultTypeProduitShouldNotBeFound("isDisabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllTypeProduitsByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);
        Products products = ProductsResourceIT.createEntity(em);
        em.persist(products);
        em.flush();
        typeProduit.addProducts(products);
        typeProduitRepository.saveAndFlush(typeProduit);
        Long productsId = products.getId();

        // Get all the typeProduitList where products equals to productsId
        defaultTypeProduitShouldBeFound("productsId.equals=" + productsId);

        // Get all the typeProduitList where products equals to productsId + 1
        defaultTypeProduitShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeProduitShouldBeFound(String filter) throws Exception {
        restTypeProduitMockMvc.perform(get("/api/type-produits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())));

        // Check, that the count call also returns 1
        restTypeProduitMockMvc.perform(get("/api/type-produits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeProduitShouldNotBeFound(String filter) throws Exception {
        restTypeProduitMockMvc.perform(get("/api/type-produits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeProduitMockMvc.perform(get("/api/type-produits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTypeProduit() throws Exception {
        // Get the typeProduit
        restTypeProduitMockMvc.perform(get("/api/type-produits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeProduit() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        int databaseSizeBeforeUpdate = typeProduitRepository.findAll().size();

        // Update the typeProduit
        TypeProduit updatedTypeProduit = typeProduitRepository.findById(typeProduit.getId()).get();
        // Disconnect from session so that the updates on updatedTypeProduit are not directly saved in db
        em.detach(updatedTypeProduit);
        updatedTypeProduit
            .libelle(UPDATED_LIBELLE)
            .isDisabled(UPDATED_IS_DISABLED);
        TypeProduitDTO typeProduitDTO = typeProduitMapper.toDto(updatedTypeProduit);

        restTypeProduitMockMvc.perform(put("/api/type-produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeProduitDTO)))
            .andExpect(status().isOk());

        // Validate the TypeProduit in the database
        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeUpdate);
        TypeProduit testTypeProduit = typeProduitList.get(typeProduitList.size() - 1);
        assertThat(testTypeProduit.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeProduit.isIsDisabled()).isEqualTo(UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeProduit() throws Exception {
        int databaseSizeBeforeUpdate = typeProduitRepository.findAll().size();

        // Create the TypeProduit
        TypeProduitDTO typeProduitDTO = typeProduitMapper.toDto(typeProduit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeProduitMockMvc.perform(put("/api/type-produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeProduitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeProduit in the database
        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeProduit() throws Exception {
        // Initialize the database
        typeProduitRepository.saveAndFlush(typeProduit);

        int databaseSizeBeforeDelete = typeProduitRepository.findAll().size();

        // Delete the typeProduit
        restTypeProduitMockMvc.perform(delete("/api/type-produits/{id}", typeProduit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeProduit> typeProduitList = typeProduitRepository.findAll();
        assertThat(typeProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
