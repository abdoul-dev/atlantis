package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.LigneEntreeStock;
import com.atlantis.bf.domain.Products;
import com.atlantis.bf.domain.EntreeStock;
import com.atlantis.bf.repository.LigneEntreeStockRepository;
import com.atlantis.bf.service.LigneEntreeStockService;
import com.atlantis.bf.service.dto.LigneEntreeStockDTO;
import com.atlantis.bf.service.mapper.LigneEntreeStockMapper;
import com.atlantis.bf.service.dto.LigneEntreeStockCriteria;
import com.atlantis.bf.service.LigneEntreeStockQueryService;

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
 * Integration tests for the {@link LigneEntreeStockResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LigneEntreeStockResourceIT {

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRIX_UNITAIRE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRIX_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRIX_TOTAL = new BigDecimal(1 - 1);

    @Autowired
    private LigneEntreeStockRepository ligneEntreeStockRepository;

    @Autowired
    private LigneEntreeStockMapper ligneEntreeStockMapper;

    @Autowired
    private LigneEntreeStockService ligneEntreeStockService;

    @Autowired
    private LigneEntreeStockQueryService ligneEntreeStockQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneEntreeStockMockMvc;

    private LigneEntreeStock ligneEntreeStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneEntreeStock createEntity(EntityManager em) {
        LigneEntreeStock ligneEntreeStock = new LigneEntreeStock()
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .quantite(DEFAULT_QUANTITE)
            .prixTotal(DEFAULT_PRIX_TOTAL);
        return ligneEntreeStock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneEntreeStock createUpdatedEntity(EntityManager em) {
        LigneEntreeStock ligneEntreeStock = new LigneEntreeStock()
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .prixTotal(UPDATED_PRIX_TOTAL);
        return ligneEntreeStock;
    }

    @BeforeEach
    public void initTest() {
        ligneEntreeStock = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneEntreeStock() throws Exception {
        int databaseSizeBeforeCreate = ligneEntreeStockRepository.findAll().size();
        // Create the LigneEntreeStock
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(ligneEntreeStock);
        restLigneEntreeStockMockMvc.perform(post("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isCreated());

        // Validate the LigneEntreeStock in the database
        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeCreate + 1);
        LigneEntreeStock testLigneEntreeStock = ligneEntreeStockList.get(ligneEntreeStockList.size() - 1);
        assertThat(testLigneEntreeStock.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testLigneEntreeStock.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneEntreeStock.getPrixTotal()).isEqualTo(DEFAULT_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void createLigneEntreeStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneEntreeStockRepository.findAll().size();

        // Create the LigneEntreeStock with an existing ID
        ligneEntreeStock.setId(1L);
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(ligneEntreeStock);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneEntreeStockMockMvc.perform(post("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneEntreeStock in the database
        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneEntreeStockRepository.findAll().size();
        // set the field null
        ligneEntreeStock.setPrixUnitaire(null);

        // Create the LigneEntreeStock, which fails.
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(ligneEntreeStock);


        restLigneEntreeStockMockMvc.perform(post("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isBadRequest());

        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneEntreeStockRepository.findAll().size();
        // set the field null
        ligneEntreeStock.setQuantite(null);

        // Create the LigneEntreeStock, which fails.
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(ligneEntreeStock);


        restLigneEntreeStockMockMvc.perform(post("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isBadRequest());

        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneEntreeStockRepository.findAll().size();
        // set the field null
        ligneEntreeStock.setPrixTotal(null);

        // Create the LigneEntreeStock, which fails.
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(ligneEntreeStock);


        restLigneEntreeStockMockMvc.perform(post("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isBadRequest());

        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocks() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneEntreeStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].prixTotal").value(hasItem(DEFAULT_PRIX_TOTAL.intValue())));
    }
    
    @Test
    @Transactional
    public void getLigneEntreeStock() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get the ligneEntreeStock
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks/{id}", ligneEntreeStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneEntreeStock.getId().intValue()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()))
            .andExpect(jsonPath("$.prixTotal").value(DEFAULT_PRIX_TOTAL.intValue()));
    }


    @Test
    @Transactional
    public void getLigneEntreeStocksByIdFiltering() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        Long id = ligneEntreeStock.getId();

        defaultLigneEntreeStockShouldBeFound("id.equals=" + id);
        defaultLigneEntreeStockShouldNotBeFound("id.notEquals=" + id);

        defaultLigneEntreeStockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLigneEntreeStockShouldNotBeFound("id.greaterThan=" + id);

        defaultLigneEntreeStockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLigneEntreeStockShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire equals to DEFAULT_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.equals=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire equals to UPDATED_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.equals=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire not equals to DEFAULT_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.notEquals=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire not equals to UPDATED_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.notEquals=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsInShouldWork() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire in DEFAULT_PRIX_UNITAIRE or UPDATED_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.in=" + DEFAULT_PRIX_UNITAIRE + "," + UPDATED_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire equals to UPDATED_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.in=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire is not null
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.specified=true");

        // Get all the ligneEntreeStockList where prixUnitaire is null
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.specified=false");
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire is greater than or equal to DEFAULT_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.greaterThanOrEqual=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire is greater than or equal to UPDATED_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.greaterThanOrEqual=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire is less than or equal to DEFAULT_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.lessThanOrEqual=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire is less than or equal to SMALLER_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.lessThanOrEqual=" + SMALLER_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsLessThanSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire is less than DEFAULT_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.lessThan=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire is less than UPDATED_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.lessThan=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixUnitaireIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixUnitaire is greater than DEFAULT_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldNotBeFound("prixUnitaire.greaterThan=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the ligneEntreeStockList where prixUnitaire is greater than SMALLER_PRIX_UNITAIRE
        defaultLigneEntreeStockShouldBeFound("prixUnitaire.greaterThan=" + SMALLER_PRIX_UNITAIRE);
    }


    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite equals to DEFAULT_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.equals=" + DEFAULT_QUANTITE);

        // Get all the ligneEntreeStockList where quantite equals to UPDATED_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.equals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite not equals to DEFAULT_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.notEquals=" + DEFAULT_QUANTITE);

        // Get all the ligneEntreeStockList where quantite not equals to UPDATED_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.notEquals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsInShouldWork() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite in DEFAULT_QUANTITE or UPDATED_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.in=" + DEFAULT_QUANTITE + "," + UPDATED_QUANTITE);

        // Get all the ligneEntreeStockList where quantite equals to UPDATED_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.in=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite is not null
        defaultLigneEntreeStockShouldBeFound("quantite.specified=true");

        // Get all the ligneEntreeStockList where quantite is null
        defaultLigneEntreeStockShouldNotBeFound("quantite.specified=false");
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite is greater than or equal to DEFAULT_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.greaterThanOrEqual=" + DEFAULT_QUANTITE);

        // Get all the ligneEntreeStockList where quantite is greater than or equal to UPDATED_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.greaterThanOrEqual=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite is less than or equal to DEFAULT_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.lessThanOrEqual=" + DEFAULT_QUANTITE);

        // Get all the ligneEntreeStockList where quantite is less than or equal to SMALLER_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.lessThanOrEqual=" + SMALLER_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsLessThanSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite is less than DEFAULT_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.lessThan=" + DEFAULT_QUANTITE);

        // Get all the ligneEntreeStockList where quantite is less than UPDATED_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.lessThan=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByQuantiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where quantite is greater than DEFAULT_QUANTITE
        defaultLigneEntreeStockShouldNotBeFound("quantite.greaterThan=" + DEFAULT_QUANTITE);

        // Get all the ligneEntreeStockList where quantite is greater than SMALLER_QUANTITE
        defaultLigneEntreeStockShouldBeFound("quantite.greaterThan=" + SMALLER_QUANTITE);
    }


    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal equals to DEFAULT_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.equals=" + DEFAULT_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal equals to UPDATED_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.equals=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal not equals to DEFAULT_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.notEquals=" + DEFAULT_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal not equals to UPDATED_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.notEquals=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsInShouldWork() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal in DEFAULT_PRIX_TOTAL or UPDATED_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.in=" + DEFAULT_PRIX_TOTAL + "," + UPDATED_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal equals to UPDATED_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.in=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal is not null
        defaultLigneEntreeStockShouldBeFound("prixTotal.specified=true");

        // Get all the ligneEntreeStockList where prixTotal is null
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal is greater than or equal to DEFAULT_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.greaterThanOrEqual=" + DEFAULT_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal is greater than or equal to UPDATED_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.greaterThanOrEqual=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal is less than or equal to DEFAULT_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.lessThanOrEqual=" + DEFAULT_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal is less than or equal to SMALLER_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.lessThanOrEqual=" + SMALLER_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal is less than DEFAULT_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.lessThan=" + DEFAULT_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal is less than UPDATED_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.lessThan=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeStocksByPrixTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        // Get all the ligneEntreeStockList where prixTotal is greater than DEFAULT_PRIX_TOTAL
        defaultLigneEntreeStockShouldNotBeFound("prixTotal.greaterThan=" + DEFAULT_PRIX_TOTAL);

        // Get all the ligneEntreeStockList where prixTotal is greater than SMALLER_PRIX_TOTAL
        defaultLigneEntreeStockShouldBeFound("prixTotal.greaterThan=" + SMALLER_PRIX_TOTAL);
    }


    @Test
    @Transactional
    public void getAllLigneEntreeStocksByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);
        Products products = ProductsResourceIT.createEntity(em);
        em.persist(products);
        em.flush();
        ligneEntreeStock.setProducts(products);
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);
        Long productsId = products.getId();

        // Get all the ligneEntreeStockList where products equals to productsId
        defaultLigneEntreeStockShouldBeFound("productsId.equals=" + productsId);

        // Get all the ligneEntreeStockList where products equals to productsId + 1
        defaultLigneEntreeStockShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }


    @Test
    @Transactional
    public void getAllLigneEntreeStocksByEntreestockIsEqualToSomething() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);
        EntreeStock entreestock = EntreeStockResourceIT.createEntity(em);
        em.persist(entreestock);
        em.flush();
        ligneEntreeStock.setEntreestock(entreestock);
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);
        Long entreestockId = entreestock.getId();

        // Get all the ligneEntreeStockList where entreestock equals to entreestockId
        defaultLigneEntreeStockShouldBeFound("entreestockId.equals=" + entreestockId);

        // Get all the ligneEntreeStockList where entreestock equals to entreestockId + 1
        defaultLigneEntreeStockShouldNotBeFound("entreestockId.equals=" + (entreestockId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLigneEntreeStockShouldBeFound(String filter) throws Exception {
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneEntreeStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].prixTotal").value(hasItem(DEFAULT_PRIX_TOTAL.intValue())));

        // Check, that the count call also returns 1
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLigneEntreeStockShouldNotBeFound(String filter) throws Exception {
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLigneEntreeStock() throws Exception {
        // Get the ligneEntreeStock
        restLigneEntreeStockMockMvc.perform(get("/api/ligne-entree-stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneEntreeStock() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        int databaseSizeBeforeUpdate = ligneEntreeStockRepository.findAll().size();

        // Update the ligneEntreeStock
        LigneEntreeStock updatedLigneEntreeStock = ligneEntreeStockRepository.findById(ligneEntreeStock.getId()).get();
        // Disconnect from session so that the updates on updatedLigneEntreeStock are not directly saved in db
        em.detach(updatedLigneEntreeStock);
        updatedLigneEntreeStock
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .prixTotal(UPDATED_PRIX_TOTAL);
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(updatedLigneEntreeStock);

        restLigneEntreeStockMockMvc.perform(put("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isOk());

        // Validate the LigneEntreeStock in the database
        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeUpdate);
        LigneEntreeStock testLigneEntreeStock = ligneEntreeStockList.get(ligneEntreeStockList.size() - 1);
        assertThat(testLigneEntreeStock.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testLigneEntreeStock.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneEntreeStock.getPrixTotal()).isEqualTo(UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneEntreeStock() throws Exception {
        int databaseSizeBeforeUpdate = ligneEntreeStockRepository.findAll().size();

        // Create the LigneEntreeStock
        LigneEntreeStockDTO ligneEntreeStockDTO = ligneEntreeStockMapper.toDto(ligneEntreeStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneEntreeStockMockMvc.perform(put("/api/ligne-entree-stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneEntreeStock in the database
        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLigneEntreeStock() throws Exception {
        // Initialize the database
        ligneEntreeStockRepository.saveAndFlush(ligneEntreeStock);

        int databaseSizeBeforeDelete = ligneEntreeStockRepository.findAll().size();

        // Delete the ligneEntreeStock
        restLigneEntreeStockMockMvc.perform(delete("/api/ligne-entree-stocks/{id}", ligneEntreeStock.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigneEntreeStock> ligneEntreeStockList = ligneEntreeStockRepository.findAll();
        assertThat(ligneEntreeStockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
