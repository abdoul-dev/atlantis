package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.LignesVentesService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.LignesVentesDTO;
import com.atlantis.bf.service.dto.LignesVentesCriteria;
import com.atlantis.bf.service.LignesVentesQueryService;
import com.atlantis.bf.service.dto.LignesVentesDTO;

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
 * REST controller for managing {@link com.atlantis.bf.domain.LignesVentes}.
 */
@RestController
@RequestMapping("/api")
public class LignesVentesResource {

    private final Logger log = LoggerFactory.getLogger(LignesVentesResource.class);

    private static final String ENTITY_NAME = "lignesVentes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LignesVentesService lignesVentesService;

    private final LignesVentesQueryService lignesVentesQueryService;

    public LignesVentesResource(LignesVentesService lignesVentesService, LignesVentesQueryService lignesVentesQueryService) {
        this.lignesVentesService = lignesVentesService;
        this.lignesVentesQueryService = lignesVentesQueryService;
    }

    /**
     * {@code POST  /lignes-ventes} : Create a new lignesVentes.
     *
     * @param lignesVentesDTO the lignesVentesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lignesVentesDTO, or with status {@code 400 (Bad Request)} if the lignesVentes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lignes-ventes")
    public ResponseEntity<LignesVentesDTO> createLignesVentes(@Valid @RequestBody LignesVentesDTO lignesVentesDTO) throws URISyntaxException {
        log.debug("REST request to save LignesVentes : {}", lignesVentesDTO);
        if (lignesVentesDTO.getId() != null) {
            throw new BadRequestAlertException("A new lignesVentes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LignesVentesDTO result = lignesVentesService.save(lignesVentesDTO);
        return ResponseEntity.created(new URI("/api/lignes-ventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lignes-ventes} : Updates an existing lignesVentes.
     *
     * @param lignesVentesDTO the lignesVentesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lignesVentesDTO,
     * or with status {@code 400 (Bad Request)} if the lignesVentesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lignesVentesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lignes-ventes")
    public ResponseEntity<LignesVentesDTO> updateLignesVentes(@Valid @RequestBody LignesVentesDTO lignesVentesDTO) throws URISyntaxException {
        log.debug("REST request to update LignesVentes : {}", lignesVentesDTO);
        if (lignesVentesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LignesVentesDTO result = lignesVentesService.save(lignesVentesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lignesVentesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lignes-ventes} : get all the lignesVentes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lignesVentes in body.
     */
    @GetMapping("/lignes-ventes")
    public ResponseEntity<List<LignesVentesDTO>> getAllLignesVentes(LignesVentesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LignesVentes by criteria: {}", criteria);
        Page<LignesVentesDTO> page = lignesVentesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lignes-ventes/count} : count all the lignesVentes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lignes-ventes/count")
    public ResponseEntity<Long> countLignesVentes(LignesVentesCriteria criteria) {
        log.debug("REST request to count LignesVentes by criteria: {}", criteria);
        return ResponseEntity.ok().body(lignesVentesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lignes-ventes/:id} : get the "id" lignesVentes.
     *
     * @param id the id of the lignesVentesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lignesVentesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lignes-ventes/{id}")
    public ResponseEntity<LignesVentesDTO> getLignesVentes(@PathVariable Long id) {
        log.debug("REST request to get LignesVentes : {}", id);
        Optional<LignesVentesDTO> lignesVentesDTO = lignesVentesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lignesVentesDTO);
    }

    /**
     * {@code DELETE  /lignes-ventes/:id} : delete the "id" lignesVentes.
     *
     * @param id the id of the lignesVentesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lignes-ventes/{id}")
    public ResponseEntity<Void> deleteLignesVentes(@PathVariable Long id) {
        log.debug("REST request to delete LignesVentes : {}", id);
        lignesVentesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/ventes/orderitems/{niveauSpecId}")
    public ResponseEntity<List<LignesVentesDTO>> getAllOrderItems(@PathVariable Long niveauSpecId) {
        log.debug("REST request to get a page of Ecues");
        List<LignesVentesDTO> list = lignesVentesService.findOrderItems(niveauSpecId);
        return ResponseEntity.ok().body(list);
    }
}
