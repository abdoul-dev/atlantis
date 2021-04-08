package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.LignesVentes;
import com.atlantis.bf.domain.Products;
import com.atlantis.bf.domain.Ventes;
import com.atlantis.bf.repository.LignesVentesRepository;
import com.atlantis.bf.service.LignesVentesService;
import com.atlantis.bf.service.dto.LignesVentesDTO;
import com.atlantis.bf.service.mapper.LignesVentesMapper;
import com.atlantis.bf.service.dto.LignesVentesCriteria;
import com.atlantis.bf.service.LignesVentesQueryService;

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
 * Integration tests for the {@link LignesVentesResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LignesVentesResourceIT {

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
    private LignesVentesRepository lignesVentesRepository;

    @Autowired
    private LignesVentesMapper lignesVentesMapper;

    @Autowired
    private LignesVentesService lignesVentesService;

    @Autowired
    private LignesVentesQueryService lignesVentesQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLignesVentesMockMvc;

    private LignesVentes lignesVentes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LignesVentes createEntity(EntityManager em) {
        LignesVentes lignesVentes = new LignesVentes()
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .quantite(DEFAULT_QUANTITE)
            .prixTotal(DEFAULT_PRIX_TOTAL);
        return lignesVentes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LignesVentes createUpdatedEntity(EntityManager em) {
        LignesVentes lignesVentes = new LignesVentes()
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .prixTotal(UPDATED_PRIX_TOTAL);
        return lignesVentes;
    }

    @BeforeEach
    public void initTest() {
        lignesVentes = createEntity(em);
    }

    @Test
    @Transactional
    public void createLignesVentes() throws Exception {
        int databaseSizeBeforeCreate = lignesVentesRepository.findAll().size();
        // Create the LignesVentes
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(lignesVentes);
        restLignesVentesMockMvc.perform(post("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isCreated());

        // Validate the LignesVentes in the database
        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeCreate + 1);
        LignesVentes testLignesVentes = lignesVentesList.get(lignesVentesList.size() - 1);
        assertThat(testLignesVentes.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testLignesVentes.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLignesVentes.getPrixTotal()).isEqualTo(DEFAULT_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void createLignesVentesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lignesVentesRepository.findAll().size();

        // Create the LignesVentes with an existing ID
        lignesVentes.setId(1L);
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(lignesVentes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLignesVentesMockMvc.perform(post("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LignesVentes in the database
        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignesVentesRepository.findAll().size();
        // set the field null
        lignesVentes.setPrixUnitaire(null);

        // Create the LignesVentes, which fails.
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(lignesVentes);


        restLignesVentesMockMvc.perform(post("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isBadRequest());

        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignesVentesRepository.findAll().size();
        // set the field null
        lignesVentes.setQuantite(null);

        // Create the LignesVentes, which fails.
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(lignesVentes);


        restLignesVentesMockMvc.perform(post("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isBadRequest());

        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignesVentesRepository.findAll().size();
        // set the field null
        lignesVentes.setPrixTotal(null);

        // Create the LignesVentes, which fails.
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(lignesVentes);


        restLignesVentesMockMvc.perform(post("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isBadRequest());

        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLignesVentes() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignesVentes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].prixTotal").value(hasItem(DEFAULT_PRIX_TOTAL.intValue())));
    }
    
    @Test
    @Transactional
    public void getLignesVentes() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get the lignesVentes
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes/{id}", lignesVentes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lignesVentes.getId().intValue()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()))
            .andExpect(jsonPath("$.prixTotal").value(DEFAULT_PRIX_TOTAL.intValue()));
    }


    @Test
    @Transactional
    public void getLignesVentesByIdFiltering() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        Long id = lignesVentes.getId();

        defaultLignesVentesShouldBeFound("id.equals=" + id);
        defaultLignesVentesShouldNotBeFound("id.notEquals=" + id);

        defaultLignesVentesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLignesVentesShouldNotBeFound("id.greaterThan=" + id);

        defaultLignesVentesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLignesVentesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire equals to DEFAULT_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.equals=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire equals to UPDATED_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.equals=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire not equals to DEFAULT_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.notEquals=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire not equals to UPDATED_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.notEquals=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsInShouldWork() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire in DEFAULT_PRIX_UNITAIRE or UPDATED_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.in=" + DEFAULT_PRIX_UNITAIRE + "," + UPDATED_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire equals to UPDATED_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.in=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire is not null
        defaultLignesVentesShouldBeFound("prixUnitaire.specified=true");

        // Get all the lignesVentesList where prixUnitaire is null
        defaultLignesVentesShouldNotBeFound("prixUnitaire.specified=false");
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire is greater than or equal to DEFAULT_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.greaterThanOrEqual=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire is greater than or equal to UPDATED_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.greaterThanOrEqual=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire is less than or equal to DEFAULT_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.lessThanOrEqual=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire is less than or equal to SMALLER_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.lessThanOrEqual=" + SMALLER_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsLessThanSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire is less than DEFAULT_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.lessThan=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire is less than UPDATED_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.lessThan=" + UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixUnitaireIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixUnitaire is greater than DEFAULT_PRIX_UNITAIRE
        defaultLignesVentesShouldNotBeFound("prixUnitaire.greaterThan=" + DEFAULT_PRIX_UNITAIRE);

        // Get all the lignesVentesList where prixUnitaire is greater than SMALLER_PRIX_UNITAIRE
        defaultLignesVentesShouldBeFound("prixUnitaire.greaterThan=" + SMALLER_PRIX_UNITAIRE);
    }


    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite equals to DEFAULT_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.equals=" + DEFAULT_QUANTITE);

        // Get all the lignesVentesList where quantite equals to UPDATED_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.equals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite not equals to DEFAULT_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.notEquals=" + DEFAULT_QUANTITE);

        // Get all the lignesVentesList where quantite not equals to UPDATED_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.notEquals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsInShouldWork() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite in DEFAULT_QUANTITE or UPDATED_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.in=" + DEFAULT_QUANTITE + "," + UPDATED_QUANTITE);

        // Get all the lignesVentesList where quantite equals to UPDATED_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.in=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite is not null
        defaultLignesVentesShouldBeFound("quantite.specified=true");

        // Get all the lignesVentesList where quantite is null
        defaultLignesVentesShouldNotBeFound("quantite.specified=false");
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite is greater than or equal to DEFAULT_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.greaterThanOrEqual=" + DEFAULT_QUANTITE);

        // Get all the lignesVentesList where quantite is greater than or equal to UPDATED_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.greaterThanOrEqual=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite is less than or equal to DEFAULT_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.lessThanOrEqual=" + DEFAULT_QUANTITE);

        // Get all the lignesVentesList where quantite is less than or equal to SMALLER_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.lessThanOrEqual=" + SMALLER_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsLessThanSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite is less than DEFAULT_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.lessThan=" + DEFAULT_QUANTITE);

        // Get all the lignesVentesList where quantite is less than UPDATED_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.lessThan=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByQuantiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where quantite is greater than DEFAULT_QUANTITE
        defaultLignesVentesShouldNotBeFound("quantite.greaterThan=" + DEFAULT_QUANTITE);

        // Get all the lignesVentesList where quantite is greater than SMALLER_QUANTITE
        defaultLignesVentesShouldBeFound("quantite.greaterThan=" + SMALLER_QUANTITE);
    }


    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal equals to DEFAULT_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.equals=" + DEFAULT_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal equals to UPDATED_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.equals=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal not equals to DEFAULT_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.notEquals=" + DEFAULT_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal not equals to UPDATED_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.notEquals=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsInShouldWork() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal in DEFAULT_PRIX_TOTAL or UPDATED_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.in=" + DEFAULT_PRIX_TOTAL + "," + UPDATED_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal equals to UPDATED_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.in=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal is not null
        defaultLignesVentesShouldBeFound("prixTotal.specified=true");

        // Get all the lignesVentesList where prixTotal is null
        defaultLignesVentesShouldNotBeFound("prixTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal is greater than or equal to DEFAULT_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.greaterThanOrEqual=" + DEFAULT_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal is greater than or equal to UPDATED_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.greaterThanOrEqual=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal is less than or equal to DEFAULT_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.lessThanOrEqual=" + DEFAULT_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal is less than or equal to SMALLER_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.lessThanOrEqual=" + SMALLER_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal is less than DEFAULT_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.lessThan=" + DEFAULT_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal is less than UPDATED_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.lessThan=" + UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void getAllLignesVentesByPrixTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        // Get all the lignesVentesList where prixTotal is greater than DEFAULT_PRIX_TOTAL
        defaultLignesVentesShouldNotBeFound("prixTotal.greaterThan=" + DEFAULT_PRIX_TOTAL);

        // Get all the lignesVentesList where prixTotal is greater than SMALLER_PRIX_TOTAL
        defaultLignesVentesShouldBeFound("prixTotal.greaterThan=" + SMALLER_PRIX_TOTAL);
    }


    @Test
    @Transactional
    public void getAllLignesVentesByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);
        Products products = ProductsResourceIT.createEntity(em);
        em.persist(products);
        em.flush();
        lignesVentes.setProducts(products);
        lignesVentesRepository.saveAndFlush(lignesVentes);
        Long productsId = products.getId();

        // Get all the lignesVentesList where products equals to productsId
        defaultLignesVentesShouldBeFound("productsId.equals=" + productsId);

        // Get all the lignesVentesList where products equals to productsId + 1
        defaultLignesVentesShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }


    @Test
    @Transactional
    public void getAllLignesVentesByVentesIsEqualToSomething() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);
        Ventes ventes = VentesResourceIT.createEntity(em);
        em.persist(ventes);
        em.flush();
        lignesVentes.setVentes(ventes);
        lignesVentesRepository.saveAndFlush(lignesVentes);
        Long ventesId = ventes.getId();

        // Get all the lignesVentesList where ventes equals to ventesId
        defaultLignesVentesShouldBeFound("ventesId.equals=" + ventesId);

        // Get all the lignesVentesList where ventes equals to ventesId + 1
        defaultLignesVentesShouldNotBeFound("ventesId.equals=" + (ventesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLignesVentesShouldBeFound(String filter) throws Exception {
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignesVentes.getId().intValue())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].prixTotal").value(hasItem(DEFAULT_PRIX_TOTAL.intValue())));

        // Check, that the count call also returns 1
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLignesVentesShouldNotBeFound(String filter) throws Exception {
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLignesVentes() throws Exception {
        // Get the lignesVentes
        restLignesVentesMockMvc.perform(get("/api/lignes-ventes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLignesVentes() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        int databaseSizeBeforeUpdate = lignesVentesRepository.findAll().size();

        // Update the lignesVentes
        LignesVentes updatedLignesVentes = lignesVentesRepository.findById(lignesVentes.getId()).get();
        // Disconnect from session so that the updates on updatedLignesVentes are not directly saved in db
        em.detach(updatedLignesVentes);
        updatedLignesVentes
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .prixTotal(UPDATED_PRIX_TOTAL);
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(updatedLignesVentes);

        restLignesVentesMockMvc.perform(put("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isOk());

        // Validate the LignesVentes in the database
        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeUpdate);
        LignesVentes testLignesVentes = lignesVentesList.get(lignesVentesList.size() - 1);
        assertThat(testLignesVentes.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testLignesVentes.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLignesVentes.getPrixTotal()).isEqualTo(UPDATED_PRIX_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingLignesVentes() throws Exception {
        int databaseSizeBeforeUpdate = lignesVentesRepository.findAll().size();

        // Create the LignesVentes
        LignesVentesDTO lignesVentesDTO = lignesVentesMapper.toDto(lignesVentes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLignesVentesMockMvc.perform(put("/api/lignes-ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lignesVentesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LignesVentes in the database
        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLignesVentes() throws Exception {
        // Initialize the database
        lignesVentesRepository.saveAndFlush(lignesVentes);

        int databaseSizeBeforeDelete = lignesVentesRepository.findAll().size();

        // Delete the lignesVentes
        restLignesVentesMockMvc.perform(delete("/api/lignes-ventes/{id}", lignesVentes.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LignesVentes> lignesVentesList = lignesVentesRepository.findAll();
        assertThat(lignesVentesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
