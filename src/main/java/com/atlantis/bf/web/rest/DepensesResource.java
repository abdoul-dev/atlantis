package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.DepensesService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.DepensesDTO;
import com.atlantis.bf.service.dto.DepensesCriteria;
import com.atlantis.bf.service.DepensesQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.compress.utils.IOUtils;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.atlantis.bf.domain.Depenses}.
 */
@RestController
@RequestMapping("/api")
public class DepensesResource {

    private final Logger log = LoggerFactory.getLogger(DepensesResource.class);

    private static final String ENTITY_NAME = "depenses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepensesService depensesService;

    private final DepensesQueryService depensesQueryService;

    public DepensesResource(DepensesService depensesService, DepensesQueryService depensesQueryService) {
        this.depensesService = depensesService;
        this.depensesQueryService = depensesQueryService;
    }

    /**
     * {@code POST  /depenses} : Create a new depenses.
     *
     * @param depensesDTO the depensesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depensesDTO, or with status {@code 400 (Bad Request)} if the depenses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depenses")
    public ResponseEntity<DepensesDTO> createDepenses(@Valid @RequestBody DepensesDTO depensesDTO) throws URISyntaxException {
        log.debug("REST request to save Depenses : {}", depensesDTO);
        if (depensesDTO.getId() != null) {
            throw new BadRequestAlertException("A new depenses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepensesDTO result = depensesService.save(depensesDTO);
        return ResponseEntity.created(new URI("/api/depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depenses} : Updates an existing depenses.
     *
     * @param depensesDTO the depensesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depensesDTO,
     * or with status {@code 400 (Bad Request)} if the depensesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depensesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depenses")
    public ResponseEntity<DepensesDTO> updateDepenses(@Valid @RequestBody DepensesDTO depensesDTO) throws URISyntaxException {
        log.debug("REST request to update Depenses : {}", depensesDTO);
        if (depensesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepensesDTO result = depensesService.save(depensesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depensesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /depenses} : get all the depenses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depenses in body.
     */
    @GetMapping("/depenses")
    public ResponseEntity<List<DepensesDTO>> getAllDepenses(DepensesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Depenses by criteria: {}", criteria);
        Page<DepensesDTO> page = depensesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depenses/count} : count all the depenses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depenses/count")
    public ResponseEntity<Long> countDepenses(DepensesCriteria criteria) {
        log.debug("REST request to count Depenses by criteria: {}", criteria);
        return ResponseEntity.ok().body(depensesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depenses/:id} : get the "id" depenses.
     *
     * @param id the id of the depensesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depensesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depenses/{id}")
    public ResponseEntity<DepensesDTO> getDepenses(@PathVariable Long id) {
        log.debug("REST request to get Depenses : {}", id);
        Optional<DepensesDTO> depensesDTO = depensesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depensesDTO);
    }

    /**
     * {@code DELETE  /depenses/:id} : delete the "id" depenses.
     *
     * @param id the id of the depensesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depenses/{id}")
    public ResponseEntity<Void> deleteDepenses(@PathVariable Long id) {
        log.debug("REST request to delete Depenses : {}", id);
        depensesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/depenses/exportAllDepenses")
    public ResponseEntity<byte[]> exportAllDepenses(
        @RequestParam(name = "dateDebut") LocalDate dateDebut,
        @RequestParam(name = "dateFin") LocalDate dateFin
        ) throws Exception {
      log.debug("obtenir la vente du jour");
      return depensesService.exportAllDepenses(dateDebut, dateFin);
    }

    @GetMapping("/depenses/exportDepensesByType")
    public ResponseEntity<byte[]> exportAllDepensesByType(
        @RequestParam(name = "dateDebut") LocalDate dateDebut,
        @RequestParam(name = "dateFin") LocalDate dateFin,
        @RequestParam(name = "typeDepenseId") Long typeDepenseId
        ) throws Exception {
      log.debug("obtenir la vente du jour");
      return depensesService.exportAllDepensesByType(dateDebut, dateFin, typeDepenseId);
    }
}
