package com.atlantis.bf.web.rest;

import com.atlantis.bf.AtlantisPoissonnerieApp;
import com.atlantis.bf.domain.Reservation;
import com.atlantis.bf.domain.Client;
import com.atlantis.bf.repository.ReservationRepository;
import com.atlantis.bf.service.ReservationService;
import com.atlantis.bf.service.dto.ReservationDTO;
import com.atlantis.bf.service.mapper.ReservationMapper;
import com.atlantis.bf.service.dto.ReservationCriteria;
import com.atlantis.bf.service.ReservationQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@SpringBootTest(classes = AtlantisPoissonnerieApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReservationResourceIT {

    private static final LocalDate DEFAULT_REVERVE_POUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REVERVE_POUR = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REVERVE_POUR = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ANNULE = false;
    private static final Boolean UPDATED_ANNULE = true;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationQueryService reservationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .revervePour(DEFAULT_REVERVE_POUR)
            .date(DEFAULT_DATE)
            .annule(DEFAULT_ANNULE);
        return reservation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createUpdatedEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .revervePour(UPDATED_REVERVE_POUR)
            .date(UPDATED_DATE)
            .annule(UPDATED_ANNULE);
        return reservation;
    }

    @BeforeEach
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();
        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getRevervePour()).isEqualTo(DEFAULT_REVERVE_POUR);
        assertThat(testReservation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReservation.isAnnule()).isEqualTo(DEFAULT_ANNULE);
    }

    @Test
    @Transactional
    public void createReservationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation with an existing ID
        reservation.setId(1L);
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRevervePourIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setRevervePour(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);


        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setDate(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);


        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnuleIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationRepository.findAll().size();
        // set the field null
        reservation.setAnnule(null);

        // Create the Reservation, which fails.
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);


        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].revervePour").value(hasItem(DEFAULT_REVERVE_POUR.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.revervePour").value(DEFAULT_REVERVE_POUR.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.annule").value(DEFAULT_ANNULE.booleanValue()));
    }


    @Test
    @Transactional
    public void getReservationsByIdFiltering() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        Long id = reservation.getId();

        defaultReservationShouldBeFound("id.equals=" + id);
        defaultReservationShouldNotBeFound("id.notEquals=" + id);

        defaultReservationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReservationShouldNotBeFound("id.greaterThan=" + id);

        defaultReservationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReservationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour equals to DEFAULT_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.equals=" + DEFAULT_REVERVE_POUR);

        // Get all the reservationList where revervePour equals to UPDATED_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.equals=" + UPDATED_REVERVE_POUR);
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour not equals to DEFAULT_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.notEquals=" + DEFAULT_REVERVE_POUR);

        // Get all the reservationList where revervePour not equals to UPDATED_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.notEquals=" + UPDATED_REVERVE_POUR);
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour in DEFAULT_REVERVE_POUR or UPDATED_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.in=" + DEFAULT_REVERVE_POUR + "," + UPDATED_REVERVE_POUR);

        // Get all the reservationList where revervePour equals to UPDATED_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.in=" + UPDATED_REVERVE_POUR);
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour is not null
        defaultReservationShouldBeFound("revervePour.specified=true");

        // Get all the reservationList where revervePour is null
        defaultReservationShouldNotBeFound("revervePour.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour is greater than or equal to DEFAULT_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.greaterThanOrEqual=" + DEFAULT_REVERVE_POUR);

        // Get all the reservationList where revervePour is greater than or equal to UPDATED_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.greaterThanOrEqual=" + UPDATED_REVERVE_POUR);
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour is less than or equal to DEFAULT_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.lessThanOrEqual=" + DEFAULT_REVERVE_POUR);

        // Get all the reservationList where revervePour is less than or equal to SMALLER_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.lessThanOrEqual=" + SMALLER_REVERVE_POUR);
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour is less than DEFAULT_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.lessThan=" + DEFAULT_REVERVE_POUR);

        // Get all the reservationList where revervePour is less than UPDATED_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.lessThan=" + UPDATED_REVERVE_POUR);
    }

    @Test
    @Transactional
    public void getAllReservationsByRevervePourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where revervePour is greater than DEFAULT_REVERVE_POUR
        defaultReservationShouldNotBeFound("revervePour.greaterThan=" + DEFAULT_REVERVE_POUR);

        // Get all the reservationList where revervePour is greater than SMALLER_REVERVE_POUR
        defaultReservationShouldBeFound("revervePour.greaterThan=" + SMALLER_REVERVE_POUR);
    }


    @Test
    @Transactional
    public void getAllReservationsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date equals to DEFAULT_DATE
        defaultReservationShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the reservationList where date equals to UPDATED_DATE
        defaultReservationShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date not equals to DEFAULT_DATE
        defaultReservationShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the reservationList where date not equals to UPDATED_DATE
        defaultReservationShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date in DEFAULT_DATE or UPDATED_DATE
        defaultReservationShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the reservationList where date equals to UPDATED_DATE
        defaultReservationShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date is not null
        defaultReservationShouldBeFound("date.specified=true");

        // Get all the reservationList where date is null
        defaultReservationShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date is greater than or equal to DEFAULT_DATE
        defaultReservationShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the reservationList where date is greater than or equal to UPDATED_DATE
        defaultReservationShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date is less than or equal to DEFAULT_DATE
        defaultReservationShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the reservationList where date is less than or equal to SMALLER_DATE
        defaultReservationShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date is less than DEFAULT_DATE
        defaultReservationShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the reservationList where date is less than UPDATED_DATE
        defaultReservationShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReservationsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where date is greater than DEFAULT_DATE
        defaultReservationShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the reservationList where date is greater than SMALLER_DATE
        defaultReservationShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllReservationsByAnnuleIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where annule equals to DEFAULT_ANNULE
        defaultReservationShouldBeFound("annule.equals=" + DEFAULT_ANNULE);

        // Get all the reservationList where annule equals to UPDATED_ANNULE
        defaultReservationShouldNotBeFound("annule.equals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllReservationsByAnnuleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where annule not equals to DEFAULT_ANNULE
        defaultReservationShouldNotBeFound("annule.notEquals=" + DEFAULT_ANNULE);

        // Get all the reservationList where annule not equals to UPDATED_ANNULE
        defaultReservationShouldBeFound("annule.notEquals=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllReservationsByAnnuleIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where annule in DEFAULT_ANNULE or UPDATED_ANNULE
        defaultReservationShouldBeFound("annule.in=" + DEFAULT_ANNULE + "," + UPDATED_ANNULE);

        // Get all the reservationList where annule equals to UPDATED_ANNULE
        defaultReservationShouldNotBeFound("annule.in=" + UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void getAllReservationsByAnnuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where annule is not null
        defaultReservationShouldBeFound("annule.specified=true");

        // Get all the reservationList where annule is null
        defaultReservationShouldNotBeFound("annule.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);
        Client client = ClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        reservation.setClient(client);
        reservationRepository.saveAndFlush(reservation);
        Long clientId = client.getId();

        // Get all the reservationList where client equals to clientId
        defaultReservationShouldBeFound("clientId.equals=" + clientId);

        // Get all the reservationList where client equals to clientId + 1
        defaultReservationShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].revervePour").value(hasItem(DEFAULT_REVERVE_POUR.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].annule").value(hasItem(DEFAULT_ANNULE.booleanValue())));

        // Check, that the count call also returns 1
        restReservationMockMvc.perform(get("/api/reservations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReservationMockMvc.perform(get("/api/reservations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .revervePour(UPDATED_REVERVE_POUR)
            .date(UPDATED_DATE)
            .annule(UPDATED_ANNULE);
        ReservationDTO reservationDTO = reservationMapper.toDto(updatedReservation);

        restReservationMockMvc.perform(put("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getRevervePour()).isEqualTo(UPDATED_REVERVE_POUR);
        assertThat(testReservation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReservation.isAnnule()).isEqualTo(UPDATED_ANNULE);
    }

    @Test
    @Transactional
    public void updateNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc.perform(put("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc.perform(delete("/api/reservations/{id}", reservation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
