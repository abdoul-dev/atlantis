package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.VentesService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.VentesDTO;
import com.atlantis.bf.service.dto.LignesVentesDTO;
import com.atlantis.bf.service.dto.VentesCriteria;
import com.atlantis.bf.service.VentesQueryService;

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
 * REST controller for managing {@link com.atlantis.bf.domain.Ventes}.
 */
@RestController
@RequestMapping("/api")
public class VentesResource {

    private final Logger log = LoggerFactory.getLogger(VentesResource.class);

    private static final String ENTITY_NAME = "ventes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentesService ventesService;

    private final VentesQueryService ventesQueryService;

    public VentesResource(VentesService ventesService, VentesQueryService ventesQueryService) {
        this.ventesService = ventesService;
        this.ventesQueryService = ventesQueryService;
    }

    /**
     * {@code POST  /ventes} : Create a new ventes.
     *
     * @param ventesDTO the ventesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ventesDTO, or with status {@code 400 (Bad Request)} if the ventes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ventes")
    public ResponseEntity<VentesDTO> createVentes(@Valid @RequestBody VentesDTO ventesDTO) throws URISyntaxException {
        log.debug("REST request to save Ventes : {}", ventesDTO);
        if (ventesDTO.getId() != null) {
            throw new BadRequestAlertException("A new ventes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VentesDTO result = ventesService.save(ventesDTO);
        return ResponseEntity.created(new URI("/api/ventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ventes} : Updates an existing ventes.
     *
     * @param ventesDTO the ventesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventesDTO,
     * or with status {@code 400 (Bad Request)} if the ventesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ventesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventes")
    public ResponseEntity<VentesDTO> updateVentes(@Valid @RequestBody VentesDTO ventesDTO) throws URISyntaxException {
        log.debug("REST request to update Ventes : {}", ventesDTO);
        if (ventesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VentesDTO result = ventesService.save(ventesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ventesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ventes} : get all the ventes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventes in body.
     */
    @GetMapping("/ventes")
    public ResponseEntity<List<VentesDTO>> getAllVentes(VentesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ventes by criteria: {}", criteria);
        Page<VentesDTO> page = ventesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ventes/count} : count all the ventes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ventes/count")
    public ResponseEntity<Long> countVentes(VentesCriteria criteria) {
        log.debug("REST request to count Ventes by criteria: {}", criteria);
        return ResponseEntity.ok().body(ventesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ventes/:id} : get the "id" ventes.
     *
     * @param id the id of the ventesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ventesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventes/{id}")
    public ResponseEntity<VentesDTO> getVentes(@PathVariable Long id) {
        log.debug("REST request to get Ventes : {}", id);
        Optional<VentesDTO> ventesDTO = ventesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ventesDTO);
    }

    /**
     * {@code DELETE  /ventes/:id} : delete the "id" ventes.
     *
     * @param id the id of the ventesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventes/{id}")
    public ResponseEntity<Void> deleteVentes(@PathVariable Long id) {
        log.debug("REST request to delete Ventes : {}", id);
        ventesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
