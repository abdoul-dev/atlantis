package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.EntreeStockService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.EntreeStockDTO;
import com.atlantis.bf.service.dto.EntreeStockCriteria;
import com.atlantis.bf.service.EntreeStockQueryService;

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
 * REST controller for managing {@link com.atlantis.bf.domain.EntreeStock}.
 */
@RestController
@RequestMapping("/api")
public class EntreeStockResource {

    private final Logger log = LoggerFactory.getLogger(EntreeStockResource.class);

    private static final String ENTITY_NAME = "entreeStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntreeStockService entreeStockService;

    private final EntreeStockQueryService entreeStockQueryService;

    public EntreeStockResource(EntreeStockService entreeStockService, EntreeStockQueryService entreeStockQueryService) {
        this.entreeStockService = entreeStockService;
        this.entreeStockQueryService = entreeStockQueryService;
    }

    /**
     * {@code POST  /entree-stocks} : Create a new entreeStock.
     *
     * @param entreeStockDTO the entreeStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entreeStockDTO, or with status {@code 400 (Bad Request)} if the entreeStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entree-stocks")
    public ResponseEntity<EntreeStockDTO> createEntreeStock(@Valid @RequestBody EntreeStockDTO entreeStockDTO) throws URISyntaxException {
        log.debug("REST request to save EntreeStock : {}", entreeStockDTO);
        if (entreeStockDTO.getId() != null) {
            throw new BadRequestAlertException("A new entreeStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntreeStockDTO result = entreeStockService.save(entreeStockDTO);
        return ResponseEntity.created(new URI("/api/entree-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entree-stocks} : Updates an existing entreeStock.
     *
     * @param entreeStockDTO the entreeStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entreeStockDTO,
     * or with status {@code 400 (Bad Request)} if the entreeStockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entreeStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entree-stocks")
    public ResponseEntity<EntreeStockDTO> updateEntreeStock(@Valid @RequestBody EntreeStockDTO entreeStockDTO) throws URISyntaxException {
        log.debug("REST request to update EntreeStock : {}", entreeStockDTO);
        if (entreeStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntreeStockDTO result = entreeStockService.save(entreeStockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entreeStockDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entree-stocks} : get all the entreeStocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entreeStocks in body.
     */
    @GetMapping("/entree-stocks")
    public ResponseEntity<List<EntreeStockDTO>> getAllEntreeStocks(EntreeStockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EntreeStocks by criteria: {}", criteria);
        Page<EntreeStockDTO> page = entreeStockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entree-stocks/count} : count all the entreeStocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/entree-stocks/count")
    public ResponseEntity<Long> countEntreeStocks(EntreeStockCriteria criteria) {
        log.debug("REST request to count EntreeStocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(entreeStockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entree-stocks/:id} : get the "id" entreeStock.
     *
     * @param id the id of the entreeStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entreeStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entree-stocks/{id}")
    public ResponseEntity<EntreeStockDTO> getEntreeStock(@PathVariable Long id) {
        log.debug("REST request to get EntreeStock : {}", id);
        Optional<EntreeStockDTO> entreeStockDTO = entreeStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entreeStockDTO);
    }

    /**
     * {@code DELETE  /entree-stocks/:id} : delete the "id" entreeStock.
     *
     * @param id the id of the entreeStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entree-stocks/{id}")
    public ResponseEntity<Void> deleteEntreeStock(@PathVariable Long id) {
        log.debug("REST request to delete EntreeStock : {}", id);
        entreeStockService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
