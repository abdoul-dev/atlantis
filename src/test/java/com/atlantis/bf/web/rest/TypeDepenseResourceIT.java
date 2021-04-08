package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.TypeDepense;
import com.atlantis.bf.domain.Depenses;
import com.atlantis.bf.repository.TypeDepenseRepository;
import com.atlantis.bf.service.TypeDepenseService;
import com.atlantis.bf.service.dto.TypeDepenseDTO;
import com.atlantis.bf.service.mapper.TypeDepenseMapper;
import com.atlantis.bf.service.dto.TypeDepenseCriteria;
import com.atlantis.bf.service.TypeDepenseQueryService;

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
 * Integration tests for the {@link TypeDepenseResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TypeDepenseResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DISABLED = false;
    private static final Boolean UPDATED_IS_DISABLED = true;

    @Autowired
    private TypeDepenseRepository typeDepenseRepository;

    @Autowired
    private TypeDepenseMapper typeDepenseMapper;

    @Autowired
    private TypeDepenseService typeDepenseService;

    @Autowired
    private TypeDepenseQueryService typeDepenseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeDepenseMockMvc;

    private TypeDepense typeDepense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDepense createEntity(EntityManager em) {
        TypeDepense typeDepense = new TypeDepense()
            .libelle(DEFAULT_LIBELLE)
            .isDisabled(DEFAULT_IS_DISABLED);
        return typeDepense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDepense createUpdatedEntity(EntityManager em) {
        TypeDepense typeDepense = new TypeDepense()
            .libelle(UPDATED_LIBELLE)
            .isDisabled(UPDATED_IS_DISABLED);
        return typeDepense;
    }

    @BeforeEach
    public void initTest() {
        typeDepense = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeDepense() throws Exception {
        int databaseSizeBeforeCreate = typeDepenseRepository.findAll().size();
        // Create the TypeDepense
        TypeDepenseDTO typeDepenseDTO = typeDepenseMapper.toDto(typeDepense);
        restTypeDepenseMockMvc.perform(post("/api/type-depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeDepenseDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeDepense in the database
        List<TypeDepense> typeDepenseList = typeDepenseRepository.findAll();
        assertThat(typeDepenseList).hasSize(databaseSizeBeforeCreate + 1);
        TypeDepense testTypeDepense = typeDepenseList.get(typeDepenseList.size() - 1);
        assertThat(testTypeDepense.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeDepense.isIsDisabled()).isEqualTo(DEFAULT_IS_DISABLED);
    }

    @Test
    @Transactional
    public void createTypeDepenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeDepenseRepository.findAll().size();

        // Create the TypeDepense with an existing ID
        typeDepense.setId(1L);
        TypeDepenseDTO typeDepenseDTO = typeDepenseMapper.toDto(typeDepense);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDepenseMockMvc.perform(post("/api/type-depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeDepenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeDepense in the database
        List<TypeDepense> typeDepenseList = typeDepenseRepository.findAll();
        assertThat(typeDepenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIsDisabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeDepenseRepository.findAll().size();
        // set the field null
        typeDepense.setIsDisabled(null);

        // Create the TypeDepense, which fails.
        TypeDepenseDTO typeDepenseDTO = typeDepenseMapper.toDto(typeDepense);


        restTypeDepenseMockMvc.perform(post("/api/type-depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeDepenseDTO)))
            .andExpect(status().isBadRequest());

        List<TypeDepense> typeDepenseList = typeDepenseRepository.findAll();
        assertThat(typeDepenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeDepenses() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList
        restTypeDepenseMockMvc.perform(get("/api/type-depenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTypeDepense() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get the typeDepense
        restTypeDepenseMockMvc.perform(get("/api/type-depenses/{id}", typeDepense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeDepense.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.isDisabled").value(DEFAULT_IS_DISABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getTypeDepensesByIdFiltering() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        Long id = typeDepense.getId();

        defaultTypeDepenseShouldBeFound("id.equals=" + id);
        defaultTypeDepenseShouldNotBeFound("id.notEquals=" + id);

        defaultTypeDepenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeDepenseShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeDepenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeDepenseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTypeDepensesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where libelle equals to DEFAULT_LIBELLE
        defaultTypeDepenseShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the typeDepenseList where libelle equals to UPDATED_LIBELLE
        defaultTypeDepenseShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where libelle not equals to DEFAULT_LIBELLE
        defaultTypeDepenseShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the typeDepenseList where libelle not equals to UPDATED_LIBELLE
        defaultTypeDepenseShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultTypeDepenseShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the typeDepenseList where libelle equals to UPDATED_LIBELLE
        defaultTypeDepenseShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where libelle is not null
        defaultTypeDepenseShouldBeFound("libelle.specified=true");

        // Get all the typeDepenseList where libelle is null
        defaultTypeDepenseShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllTypeDepensesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where libelle contains DEFAULT_LIBELLE
        defaultTypeDepenseShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the typeDepenseList where libelle contains UPDATED_LIBELLE
        defaultTypeDepenseShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where libelle does not contain DEFAULT_LIBELLE
        defaultTypeDepenseShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the typeDepenseList where libelle does not contain UPDATED_LIBELLE
        defaultTypeDepenseShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllTypeDepensesByIsDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where isDisabled equals to DEFAULT_IS_DISABLED
        defaultTypeDepenseShouldBeFound("isDisabled.equals=" + DEFAULT_IS_DISABLED);

        // Get all the typeDepenseList where isDisabled equals to UPDATED_IS_DISABLED
        defaultTypeDepenseShouldNotBeFound("isDisabled.equals=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByIsDisabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where isDisabled not equals to DEFAULT_IS_DISABLED
        defaultTypeDepenseShouldNotBeFound("isDisabled.notEquals=" + DEFAULT_IS_DISABLED);

        // Get all the typeDepenseList where isDisabled not equals to UPDATED_IS_DISABLED
        defaultTypeDepenseShouldBeFound("isDisabled.notEquals=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByIsDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where isDisabled in DEFAULT_IS_DISABLED or UPDATED_IS_DISABLED
        defaultTypeDepenseShouldBeFound("isDisabled.in=" + DEFAULT_IS_DISABLED + "," + UPDATED_IS_DISABLED);

        // Get all the typeDepenseList where isDisabled equals to UPDATED_IS_DISABLED
        defaultTypeDepenseShouldNotBeFound("isDisabled.in=" + UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByIsDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        // Get all the typeDepenseList where isDisabled is not null
        defaultTypeDepenseShouldBeFound("isDisabled.specified=true");

        // Get all the typeDepenseList where isDisabled is null
        defaultTypeDepenseShouldNotBeFound("isDisabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllTypeDepensesByDepensesIsEqualToSomething() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);
        Depenses depenses = DepensesResourceIT.createEntity(em);
        em.persist(depenses);
        em.flush();
        typeDepense.addDepenses(depenses);
        typeDepenseRepository.saveAndFlush(typeDepense);
        Long depensesId = depenses.getId();

        // Get all the typeDepenseList where depenses equals to depensesId
        defaultTypeDepenseShouldBeFound("depensesId.equals=" + depensesId);

        // Get all the typeDepenseList where depenses equals to depensesId + 1
        defaultTypeDepenseShouldNotBeFound("depensesId.equals=" + (depensesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeDepenseShouldBeFound(String filter) throws Exception {
        restTypeDepenseMockMvc.perform(get("/api/type-depenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].isDisabled").value(hasItem(DEFAULT_IS_DISABLED.booleanValue())));

        // Check, that the count call also returns 1
        restTypeDepenseMockMvc.perform(get("/api/type-depenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeDepenseShouldNotBeFound(String filter) throws Exception {
        restTypeDepenseMockMvc.perform(get("/api/type-depenses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeDepenseMockMvc.perform(get("/api/type-depenses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTypeDepense() throws Exception {
        // Get the typeDepense
        restTypeDepenseMockMvc.perform(get("/api/type-depenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeDepense() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        int databaseSizeBeforeUpdate = typeDepenseRepository.findAll().size();

        // Update the typeDepense
        TypeDepense updatedTypeDepense = typeDepenseRepository.findById(typeDepense.getId()).get();
        // Disconnect from session so that the updates on updatedTypeDepense are not directly saved in db
        em.detach(updatedTypeDepense);
        updatedTypeDepense
            .libelle(UPDATED_LIBELLE)
            .isDisabled(UPDATED_IS_DISABLED);
        TypeDepenseDTO typeDepenseDTO = typeDepenseMapper.toDto(updatedTypeDepense);

        restTypeDepenseMockMvc.perform(put("/api/type-depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeDepenseDTO)))
            .andExpect(status().isOk());

        // Validate the TypeDepense in the database
        List<TypeDepense> typeDepenseList = typeDepenseRepository.findAll();
        assertThat(typeDepenseList).hasSize(databaseSizeBeforeUpdate);
        TypeDepense testTypeDepense = typeDepenseList.get(typeDepenseList.size() - 1);
        assertThat(testTypeDepense.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeDepense.isIsDisabled()).isEqualTo(UPDATED_IS_DISABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeDepense() throws Exception {
        int databaseSizeBeforeUpdate = typeDepenseRepository.findAll().size();

        // Create the TypeDepense
        TypeDepenseDTO typeDepenseDTO = typeDepenseMapper.toDto(typeDepense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDepenseMockMvc.perform(put("/api/type-depenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeDepenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeDepense in the database
        List<TypeDepense> typeDepenseList = typeDepenseRepository.findAll();
        assertThat(typeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeDepense() throws Exception {
        // Initialize the database
        typeDepenseRepository.saveAndFlush(typeDepense);

        int databaseSizeBeforeDelete = typeDepenseRepository.findAll().size();

        // Delete the typeDepense
        restTypeDepenseMockMvc.perform(delete("/api/type-depenses/{id}", typeDepense.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeDepense> typeDepenseList = typeDepenseRepository.findAll();
        assertThat(typeDepenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
