package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.LignesReservationService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.LignesReservationDTO;
import com.atlantis.bf.service.dto.LignesReservationCriteria;
import com.atlantis.bf.service.LignesReservationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.atlantis.bf.domain.LignesReservation}.
 */
@RestController
@RequestMapping("/api")
public class LignesReservationResource {

    private final Logger log = LoggerFactory.getLogger(LignesReservationResource.class);

    private static final String ENTITY_NAME = "lignesReservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LignesReservationService lignesReservationService;

    private final LignesReservationQueryService lignesReservationQueryService;

    public LignesReservationResource(LignesReservationService lignesReservationService, LignesReservationQueryService lignesReservationQueryService) {
        this.lignesReservationService = lignesReservationService;
        this.lignesReservationQueryService = lignesReservationQueryService;
    }

    /**
     * {@code POST  /lignes-reservations} : Create a new lignesReservation.
     *
     * @param lignesReservationDTO the lignesReservationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lignesReservationDTO, or with status {@code 400 (Bad Request)} if the lignesReservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lignes-reservations")
    public ResponseEntity<LignesReservationDTO> createLignesReservation(@Valid @RequestBody LignesReservationDTO lignesReservationDTO) throws URISyntaxException {
        log.debug("REST request to save LignesReservation : {}", lignesReservationDTO);
        if (lignesReservationDTO.getId() != null) {
            throw new BadRequestAlertException("A new lignesReservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LignesReservationDTO result = lignesReservationService.save(lignesReservationDTO);
        return ResponseEntity.created(new URI("/api/lignes-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lignes-reservations} : Updates an existing lignesReservation.
     *
     * @param lignesReservationDTO the lignesReservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lignesReservationDTO,
     * or with status {@code 400 (Bad Request)} if the lignesReservationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lignesReservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lignes-reservations")
    public ResponseEntity<LignesReservationDTO> updateLignesReservation(@Valid @RequestBody LignesReservationDTO lignesReservationDTO) throws URISyntaxException {
        log.debug("REST request to update LignesReservation : {}", lignesReservationDTO);
        if (lignesReservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LignesReservationDTO result = lignesReservationService.save(lignesReservationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lignesReservationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lignes-reservations} : get all the lignesReservations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lignesReservations in body.
     */
    @GetMapping("/lignes-reservations")
    public ResponseEntity<List<LignesReservationDTO>> getAllLignesReservations(LignesReservationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LignesReservations by criteria: {}", criteria);
        Page<LignesReservationDTO> page = lignesReservationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lignes-reservations/count} : count all the lignesReservations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lignes-reservations/count")
    public ResponseEntity<Long> countLignesReservations(LignesReservationCriteria criteria) {
        log.debug("REST request to count LignesReservations by criteria: {}", criteria);
        return ResponseEntity.ok().body(lignesReservationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lignes-reservations/:id} : get the "id" lignesReservation.
     *
     * @param id the id of the lignesReservationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lignesReservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lignes-reservations/{id}")
    public ResponseEntity<LignesReservationDTO> getLignesReservation(@PathVariable Long id) {
        log.debug("REST request to get LignesReservation : {}", id);
        Optional<LignesReservationDTO> lignesReservationDTO = lignesReservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lignesReservationDTO);
    }

    /**
     * {@code DELETE  /lignes-reservations/:id} : delete the "id" lignesReservation.
     *
     * @param id the id of the lignesReservationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lignes-reservations/{id}")
    public ResponseEntity<Void> deleteLignesReservation(@PathVariable Long id) {
        log.debug("REST request to delete LignesReservation : {}", id);
        lignesReservationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
