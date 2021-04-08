package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.LigneEntreeStockService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.LigneEntreeStockDTO;
import com.atlantis.bf.service.dto.LigneEntreeStockCriteria;
import com.atlantis.bf.service.LigneEntreeStockQueryService;

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
 * REST controller for managing {@link com.atlantis.bf.domain.LigneEntreeStock}.
 */
@RestController
@RequestMapping("/api")
public class LigneEntreeStockResource {

    private final Logger log = LoggerFactory.getLogger(LigneEntreeStockResource.class);

    private static final String ENTITY_NAME = "ligneEntreeStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneEntreeStockService ligneEntreeStockService;

    private final LigneEntreeStockQueryService ligneEntreeStockQueryService;

    public LigneEntreeStockResource(LigneEntreeStockService ligneEntreeStockService, LigneEntreeStockQueryService ligneEntreeStockQueryService) {
        this.ligneEntreeStockService = ligneEntreeStockService;
        this.ligneEntreeStockQueryService = ligneEntreeStockQueryService;
    }

    /**
     * {@code POST  /ligne-entree-stocks} : Create a new ligneEntreeStock.
     *
     * @param ligneEntreeStockDTO the ligneEntreeStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneEntreeStockDTO, or with status {@code 400 (Bad Request)} if the ligneEntreeStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligne-entree-stocks")
    public ResponseEntity<LigneEntreeStockDTO> createLigneEntreeStock(@Valid @RequestBody LigneEntreeStockDTO ligneEntreeStockDTO) throws URISyntaxException {
        log.debug("REST request to save LigneEntreeStock : {}", ligneEntreeStockDTO);
        if (ligneEntreeStockDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneEntreeStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneEntreeStockDTO result = ligneEntreeStockService.save(ligneEntreeStockDTO);
        return ResponseEntity.created(new URI("/api/ligne-entree-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligne-entree-stocks} : Updates an existing ligneEntreeStock.
     *
     * @param ligneEntreeStockDTO the ligneEntreeStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneEntreeStockDTO,
     * or with status {@code 400 (Bad Request)} if the ligneEntreeStockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneEntreeStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligne-entree-stocks")
    public ResponseEntity<LigneEntreeStockDTO> updateLigneEntreeStock(@Valid @RequestBody LigneEntreeStockDTO ligneEntreeStockDTO) throws URISyntaxException {
        log.debug("REST request to update LigneEntreeStock : {}", ligneEntreeStockDTO);
        if (ligneEntreeStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LigneEntreeStockDTO result = ligneEntreeStockService.save(ligneEntreeStockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ligneEntreeStockDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ligne-entree-stocks} : get all the ligneEntreeStocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneEntreeStocks in body.
     */
    @GetMapping("/ligne-entree-stocks")
    public ResponseEntity<List<LigneEntreeStockDTO>> getAllLigneEntreeStocks(LigneEntreeStockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LigneEntreeStocks by criteria: {}", criteria);
        Page<LigneEntreeStockDTO> page = ligneEntreeStockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-entree-stocks/count} : count all the ligneEntreeStocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ligne-entree-stocks/count")
    public ResponseEntity<Long> countLigneEntreeStocks(LigneEntreeStockCriteria criteria) {
        log.debug("REST request to count LigneEntreeStocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(ligneEntreeStockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ligne-entree-stocks/:id} : get the "id" ligneEntreeStock.
     *
     * @param id the id of the ligneEntreeStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneEntreeStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligne-entree-stocks/{id}")
    public ResponseEntity<LigneEntreeStockDTO> getLigneEntreeStock(@PathVariable Long id) {
        log.debug("REST request to get LigneEntreeStock : {}", id);
        Optional<LigneEntreeStockDTO> ligneEntreeStockDTO = ligneEntreeStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneEntreeStockDTO);
    }

    /**
     * {@code DELETE  /ligne-entree-stocks/:id} : delete the "id" ligneEntreeStock.
     *
     * @param id the id of the ligneEntreeStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligne-entree-stocks/{id}")
    public ResponseEntity<Void> deleteLigneEntreeStock(@PathVariable Long id) {
        log.debug("REST request to delete LigneEntreeStock : {}", id);
        ligneEntreeStockService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
