package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.Ventes;
import com.atlantis.bf.domain.LignesVentes;
import com.atlantis.bf.domain.Client;
import com.atlantis.bf.repository.VentesRepository;
import com.atlantis.bf.service.VentesService;
import com.atlantis.bf.service.dto.VentesDTO;
import com.atlantis.bf.service.mapper.VentesMapper;
import com.atlantis.bf.service.dto.VentesCriteria;
import com.atlantis.bf.service.VentesQueryService;

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
 * Integration tests for the {@link VentesResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VentesResourceIT {

    private static final BigDecimal DEFAULT_MONTANT_INITIAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_INITIAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_INITIAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_REMISE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REMISE = new BigDecimal(2);
    private static final BigDecimal SMALLER_REMISE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MONTANT_FINAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_FINAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_FINAL = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ANNULE = false;
    private static final Boolean UPDATED_ANNULE = true;

    @Autowired
    private VentesRepository ventesRepository;

    @Autowired
    private VentesMapper ventesMapper;

    @Autowired
    private VentesService ventesService;

    @Autowired
    private VentesQueryService ventesQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentesMockMvc;

    private Ventes ventes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ventes createEntity(EntityManager em) {
        Ventes ventes = new Ventes()
            .montantInitial(DEFAULT_MONTANT_INITIAL)
            .remise(DEFAULT_REMISE)
            .montantFinal(DEFAULT_MONTANT_FINAL)
            .date(DEFAULT_DATE)
            .annule(DEFAULT_ANNULE);
        return ventes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ventes createUpdatedEntity(EntityManager em) {
        Ventes ventes = new Ventes()
            .montantInitial(UPDATED_MONTANT_INITIAL)
            .remise(UPDATED_REMISE)
            .montantFinal(UPDATED_MONTANT_FINAL)
            .date(UPDATED_DATE)
            .annule(UPDATED_ANNULE);
        return ventes;
    }

    @BeforeEach
    public void initTest() {
        ventes = createEntity(em);
    }

    @Test
    @Transactional
    public void createVentes() throws Exception {
        int databaseSizeBeforeCreate = ventesRepository.findAll().size();
        // Create the Ventes
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);
        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isCreated());

        // Validate the Ventes in the database
        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeCreate + 1);
        Ventes testVentes = ventesList.get(ventesList.size() - 1);
        assertThat(testVentes.getMontantInitial()).isEqualTo(DEFAULT_MONTANT_INITIAL);
        assertThat(testVentes.getRemise()).isEqualTo(DEFAULT_REMISE);
        assertThat(testVentes.getMontantFinal()).isEqualTo(DEFAULT_MONTANT_FINAL);
        assertThat(testVentes.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVentes.isAnnule()).isEqualTo(DEFAULT_ANNULE);
    }

    @Test
    @Transactional
    public void createVentesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ventesRepository.findAll().size();

        // Create the Ventes with an existing ID
        ventes.setId(1L);
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ventes in the database
        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMontantInitialIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventesRepository.findAll().size();
        // set the field null
        ventes.setMontantInitial(null);

        // Create the Ventes, which fails.
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);


        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRemiseIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventesRepository.findAll().size();
        // set the field null
        ventes.setRemise(null);

        // Create the Ventes, which fails.
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);


        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantFinalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventesRepository.findAll().size();
        // set the field null
        ventes.setMontantFinal(null);

        // Create the Ventes, which fails.
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);


        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventesRepository.findAll().size();
        // set the field null
        ventes.setDate(null);

        // Create the Ventes, which fails.
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);


        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnuleIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventesRepository.findAll().size();
        // set the field null
        ventes.setAnnule(null);

        // Create the Ventes, which fails.
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);


        restVentesMockMvc.perform(post("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVentes() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList
        restVentesMockMvc.perform(get("/api/ventes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventes.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantInitial").value(hasItem(DEFAULT_MONTANT_INITIAL.intValue())))
            .andExpect(jsonPath("$.[*].remise").value(hasItem(DEFAULT_REMISE.intValue())))
            .andExpect(jsonPath("$.[*].montantFinal").value(hasItem(DEFAULT_MONTANT_FINAL.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getVentes() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get the ventes
        restVentesMockMvc.perform(get("/api/ventes/{id}", ventes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ventes.getId().intValue()))
            .andExpect(jsonPath("$.montantInitial").value(DEFAULT_MONTANT_INITIAL.intValue()))
            .andExpect(jsonPath("$.remise").value(DEFAULT_REMISE.intValue()))
            .andExpect(jsonPath("$.montantFinal").value(DEFAULT_MONTANT_FINAL.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.annule").value(DEFAULT_ANNULE.booleanValue()));
    }


    @Test
    @Transactional
    public void getVentesByIdFiltering() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        Long id = ventes.getId();

        defaultVentesShouldBeFound("id.equals=" + id);
        defaultVentesShouldNotBeFound("id.notEquals=" + id);

        defaultVentesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVentesShouldNotBeFound("id.greaterThan=" + id);

        defaultVentesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVentesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial equals to DEFAULT_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.equals=" + DEFAULT_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial equals to UPDATED_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.equals=" + UPDATED_MONTANT_INITIAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial not equals to DEFAULT_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.notEquals=" + DEFAULT_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial not equals to UPDATED_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.notEquals=" + UPDATED_MONTANT_INITIAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsInShouldWork() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial in DEFAULT_MONTANT_INITIAL or UPDATED_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.in=" + DEFAULT_MONTANT_INITIAL + "," + UPDATED_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial equals to UPDATED_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.in=" + UPDATED_MONTANT_INITIAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial is not null
        defaultVentesShouldBeFound("montantInitial.specified=true");

        // Get all the ventesList where montantInitial is null
        defaultVentesShouldNotBeFound("montantInitial.specified=false");
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial is greater than or equal to DEFAULT_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.greaterThanOrEqual=" + DEFAULT_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial is greater than or equal to UPDATED_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.greaterThanOrEqual=" + UPDATED_MONTANT_INITIAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial is less than or equal to DEFAULT_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.lessThanOrEqual=" + DEFAULT_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial is less than or equal to SMALLER_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.lessThanOrEqual=" + SMALLER_MONTANT_INITIAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsLessThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial is less than DEFAULT_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.lessThan=" + DEFAULT_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial is less than UPDATED_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.lessThan=" + UPDATED_MONTANT_INITIAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantInitialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantInitial is greater than DEFAULT_MONTANT_INITIAL
        defaultVentesShouldNotBeFound("montantInitial.greaterThan=" + DEFAULT_MONTANT_INITIAL);

        // Get all the ventesList where montantInitial is greater than SMALLER_MONTANT_INITIAL
        defaultVentesShouldBeFound("montantInitial.greaterThan=" + SMALLER_MONTANT_INITIAL);
    }


    @Test
    @Transactional
    public void getAllVentesByRemiseIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise equals to DEFAULT_REMISE
        defaultVentesShouldBeFound("remise.equals=" + DEFAULT_REMISE);

        // Get all the ventesList where remise equals to UPDATED_REMISE
        defaultVentesShouldNotBeFound("remise.equals=" + UPDATED_REMISE);
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise not equals to DEFAULT_REMISE
        defaultVentesShouldNotBeFound("remise.notEquals=" + DEFAULT_REMISE);

        // Get all the ventesList where remise not equals to UPDATED_REMISE
        defaultVentesShouldBeFound("remise.notEquals=" + UPDATED_REMISE);
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsInShouldWork() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise in DEFAULT_REMISE or UPDATED_REMISE
        defaultVentesShouldBeFound("remise.in=" + DEFAULT_REMISE + "," + UPDATED_REMISE);

        // Get all the ventesList where remise equals to UPDATED_REMISE
        defaultVentesShouldNotBeFound("remise.in=" + UPDATED_REMISE);
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise is not null
        defaultVentesShouldBeFound("remise.specified=true");

        // Get all the ventesList where remise is null
        defaultVentesShouldNotBeFound("remise.specified=false");
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise is greater than or equal to DEFAULT_REMISE
        defaultVentesShouldBeFound("remise.greaterThanOrEqual=" + DEFAULT_REMISE);

        // Get all the ventesList where remise is greater than or equal to UPDATED_REMISE
        defaultVentesShouldNotBeFound("remise.greaterThanOrEqual=" + UPDATED_REMISE);
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise is less than or equal to DEFAULT_REMISE
        defaultVentesShouldBeFound("remise.lessThanOrEqual=" + DEFAULT_REMISE);

        // Get all the ventesList where remise is less than or equal to SMALLER_REMISE
        defaultVentesShouldNotBeFound("remise.lessThanOrEqual=" + SMALLER_REMISE);
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsLessThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise is less than DEFAULT_REMISE
        defaultVentesShouldNotBeFound("remise.lessThan=" + DEFAULT_REMISE);

        // Get all the ventesList where remise is less than UPDATED_REMISE
        defaultVentesShouldBeFound("remise.lessThan=" + UPDATED_REMISE);
    }

    @Test
    @Transactional
    public void getAllVentesByRemiseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where remise is greater than DEFAULT_REMISE
        defaultVentesShouldNotBeFound("remise.greaterThan=" + DEFAULT_REMISE);

        // Get all the ventesList where remise is greater than SMALLER_REMISE
        defaultVentesShouldBeFound("remise.greaterThan=" + SMALLER_REMISE);
    }


    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal equals to DEFAULT_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.equals=" + DEFAULT_MONTANT_FINAL);

        // Get all the ventesList where montantFinal equals to UPDATED_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.equals=" + UPDATED_MONTANT_FINAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal not equals to DEFAULT_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.notEquals=" + DEFAULT_MONTANT_FINAL);

        // Get all the ventesList where montantFinal not equals to UPDATED_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.notEquals=" + UPDATED_MONTANT_FINAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsInShouldWork() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal in DEFAULT_MONTANT_FINAL or UPDATED_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.in=" + DEFAULT_MONTANT_FINAL + "," + UPDATED_MONTANT_FINAL);

        // Get all the ventesList where montantFinal equals to UPDATED_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.in=" + UPDATED_MONTANT_FINAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal is not null
        defaultVentesShouldBeFound("montantFinal.specified=true");

        // Get all the ventesList where montantFinal is null
        defaultVentesShouldNotBeFound("montantFinal.specified=false");
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal is greater than or equal to DEFAULT_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.greaterThanOrEqual=" + DEFAULT_MONTANT_FINAL);

        // Get all the ventesList where montantFinal is greater than or equal to UPDATED_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.greaterThanOrEqual=" + UPDATED_MONTANT_FINAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal is less than or equal to DEFAULT_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.lessThanOrEqual=" + DEFAULT_MONTANT_FINAL);

        // Get all the ventesList where montantFinal is less than or equal to SMALLER_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.lessThanOrEqual=" + SMALLER_MONTANT_FINAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsLessThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal is less than DEFAULT_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.lessThan=" + DEFAULT_MONTANT_FINAL);

        // Get all the ventesList where montantFinal is less than UPDATED_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.lessThan=" + UPDATED_MONTANT_FINAL);
    }

    @Test
    @Transactional
    public void getAllVentesByMontantFinalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where montantFinal is greater than DEFAULT_MONTANT_FINAL
        defaultVentesShouldNotBeFound("montantFinal.greaterThan=" + DEFAULT_MONTANT_FINAL);

        // Get all the ventesList where montantFinal is greater than SMALLER_MONTANT_FINAL
        defaultVentesShouldBeFound("montantFinal.greaterThan=" + SMALLER_MONTANT_FINAL);
    }


    @Test
    @Transactional
    public void getAllVentesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date equals to DEFAULT_DATE
        defaultVentesShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the ventesList where date equals to UPDATED_DATE
        defaultVentesShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date not equals to DEFAULT_DATE
        defaultVentesShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the ventesList where date not equals to UPDATED_DATE
        defaultVentesShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date in DEFAULT_DATE or UPDATED_DATE
        defaultVentesShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the ventesList where date equals to UPDATED_DATE
        defaultVentesShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date is not null
        defaultVentesShouldBeFound("date.specified=true");

        // Get all the ventesList where date is null
        defaultVentesShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date is greater than or equal to DEFAULT_DATE
        defaultVentesShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the ventesList where date is greater than or equal to UPDATED_DATE
        defaultVentesShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date is less than or equal to DEFAULT_DATE
        defaultVentesShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the ventesList where date is less than or equal to SMALLER_DATE
        defaultVentesShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date is less than DEFAULT_DATE
        defaultVentesShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the ventesList where date is less than UPDATED_DATE
        defaultVentesShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllVentesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where date is greater than DEFAULT_DATE
        defaultVentesShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the ventesList where date is greater than SMALLER_DATE
        defaultVentesShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllVentesByAnnuleIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where annule equals to DEFAULT_ANNULE
        defaultVentesShouldBeFound("annule.equals=" + DEFAULT_ANNULE);

        // Get all the ventesList where annule equals to UPDATED_ANNULE
        defaultVentesShouldNotBeFound("annule.equals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllVentesByAnnuleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where annule not equals to DEFAULT_ANNULE
        defaultVentesShouldNotBeFound("annule.notEquals=" + DEFAULT_ANNULE);

        // Get all the ventesList where annule not equals to UPDATED_ANNULE
        defaultVentesShouldBeFound("annule.notEquals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllVentesByAnnuleIsInShouldWork() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where annule in DEFAULT_ANNULE or UPDATED_ANNULE
        defaultVentesShouldBeFound("annule.in=" + DEFAULT_ANNULE + "," + UPDATED_ANNULE);

        // Get all the ventesList where annule equals to UPDATED_ANNULE
        defaultVentesShouldNotBeFound("annule.in=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllVentesByAnnuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        // Get all the ventesList where annule is not null
        defaultVentesShouldBeFound("annule.specified=true");

        // Get all the ventesList where annule is null
        defaultVentesShouldNotBeFound("annule.specified=false");
    }

    @Test
    @Transactional
    public void getAllVentesByLignesVentesIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);
        LignesVentes lignesVentes = LignesVentesResourceIT.createEntity(em);
        em.persist(lignesVentes);
        em.flush();
        ventes.addLignesVentes(lignesVentes);
        ventesRepository.saveAndFlush(ventes);
        Long lignesVentesId = lignesVentes.getId();

        // Get all the ventesList where lignesVentes equals to lignesVentesId
        defaultVentesShouldBeFound("lignesVentesId.equals=" + lignesVentesId);

        // Get all the ventesList where lignesVentes equals to lignesVentesId + 1
        defaultVentesShouldNotBeFound("lignesVentesId.equals=" + (lignesVentesId + 1));
    }


    @Test
    @Transactional
    public void getAllVentesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);
        Client client = ClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        ventes.setClient(client);
        ventesRepository.saveAndFlush(ventes);
        Long clientId = client.getId();

        // Get all the ventesList where client equals to clientId
        defaultVentesShouldBeFound("clientId.equals=" + clientId);

        // Get all the ventesList where client equals to clientId + 1
        defaultVentesShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVentesShouldBeFound(String filter) throws Exception {
        restVentesMockMvc.perform(get("/api/ventes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventes.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantInitial").value(hasItem(DEFAULT_MONTANT_INITIAL.intValue())))
            .andExpect(jsonPath("$.[*].remise").value(hasItem(DEFAULT_REMISE.intValue())))
            .andExpect(jsonPath("$.[*].montantFinal").value(hasItem(DEFAULT_MONTANT_FINAL.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())));

        // Check, that the count call also returns 1
        restVentesMockMvc.perform(get("/api/ventes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVentesShouldNotBeFound(String filter) throws Exception {
        restVentesMockMvc.perform(get("/api/ventes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVentesMockMvc.perform(get("/api/ventes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingVentes() throws Exception {
        // Get the ventes
        restVentesMockMvc.perform(get("/api/ventes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVentes() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        int databaseSizeBeforeUpdate = ventesRepository.findAll().size();

        // Update the ventes
        Ventes updatedVentes = ventesRepository.findById(ventes.getId()).get();
        // Disconnect from session so that the updates on updatedVentes are not directly saved in db
        em.detach(updatedVentes);
        updatedVentes
            .montantInitial(UPDATED_MONTANT_INITIAL)
            .remise(UPDATED_REMISE)
            .montantFinal(UPDATED_MONTANT_FINAL)
            .date(UPDATED_DATE)
            .annule(UPDATED_ANNULE);
        VentesDTO ventesDTO = ventesMapper.toDto(updatedVentes);

        restVentesMockMvc.perform(put("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isOk());

        // Validate the Ventes in the database
        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeUpdate);
        Ventes testVentes = ventesList.get(ventesList.size() - 1);
        assertThat(testVentes.getMontantInitial()).isEqualTo(UPDATED_MONTANT_INITIAL);
        assertThat(testVentes.getRemise()).isEqualTo(UPDATED_REMISE);
        assertThat(testVentes.getMontantFinal()).isEqualTo(UPDATED_MONTANT_FINAL);
        assertThat(testVentes.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVentes.isAnnule()).isEqualTo(UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void updateNonExistingVentes() throws Exception {
        int databaseSizeBeforeUpdate = ventesRepository.findAll().size();

        // Create the Ventes
        VentesDTO ventesDTO = ventesMapper.toDto(ventes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentesMockMvc.perform(put("/api/ventes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ventesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ventes in the database
        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVentes() throws Exception {
        // Initialize the database
        ventesRepository.saveAndFlush(ventes);

        int databaseSizeBeforeDelete = ventesRepository.findAll().size();

        // Delete the ventes
        restVentesMockMvc.perform(delete("/api/ventes/{id}", ventes.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ventes> ventesList = ventesRepository.findAll();
        assertThat(ventesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
