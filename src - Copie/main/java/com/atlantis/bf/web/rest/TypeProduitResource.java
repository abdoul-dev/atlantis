package com.atlantis.bf.web.rest;

import com.atlantis.bf.service.TypeProduitService;
import com.atlantis.bf.web.rest.errors.BadRequestAlertException;
import com.atlantis.bf.service.dto.TypeProduitDTO;
import com.atlantis.bf.service.dto.TypeProduitCriteria;
import com.atlantis.bf.service.TypeProduitQueryService;

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
 * REST controller for managing {@link com.atlantis.bf.domain.TypeProduit}.
 */
@RestController
@RequestMapping("/api")
public class TypeProduitResource {

    private final Logger log = LoggerFactory.getLogger(TypeProduitResource.class);

    private static final String ENTITY_NAME = "typeProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeProduitService typeProduitService;

    private final TypeProduitQueryService typeProduitQueryService;

    public TypeProduitResource(TypeProduitService typeProduitService, TypeProduitQueryService typeProduitQueryService) {
        this.typeProduitService = typeProduitService;
        this.typeProduitQueryService = typeProduitQueryService;
    }

    /**
     * {@code POST  /type-produits} : Create a new typeProduit.
     *
     * @param typeProduitDTO the typeProduitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeProduitDTO, or with status {@code 400 (Bad Request)} if the typeProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-produits")
    public ResponseEntity<TypeProduitDTO> createTypeProduit(@Valid @RequestBody TypeProduitDTO typeProduitDTO) throws URISyntaxException {
        log.debug("REST request to save TypeProduit : {}", typeProduitDTO);
        if (typeProduitDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeProduitDTO result = typeProduitService.save(typeProduitDTO);
        return ResponseEntity.created(new URI("/api/type-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-produits} : Updates an existing typeProduit.
     *
     * @param typeProduitDTO the typeProduitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeProduitDTO,
     * or with status {@code 400 (Bad Request)} if the typeProduitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeProduitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-produits")
    public ResponseEntity<TypeProduitDTO> updateTypeProduit(@Valid @RequestBody TypeProduitDTO typeProduitDTO) throws URISyntaxException {
        log.debug("REST request to update TypeProduit : {}", typeProduitDTO);
        if (typeProduitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeProduitDTO result = typeProduitService.save(typeProduitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeProduitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-produits} : get all the typeProduits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeProduits in body.
     */
    @GetMapping("/type-produits")
    public ResponseEntity<List<TypeProduitDTO>> getAllTypeProduits(TypeProduitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TypeProduits by criteria: {}", criteria);
        Page<TypeProduitDTO> page = typeProduitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-produits/count} : count all the typeProduits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-produits/count")
    public ResponseEntity<Long> countTypeProduits(TypeProduitCriteria criteria) {
        log.debug("REST request to count TypeProduits by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeProduitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-produits/:id} : get the "id" typeProduit.
     *
     * @param id the id of the typeProduitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeProduitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-produits/{id}")
    public ResponseEntity<TypeProduitDTO> getTypeProduit(@PathVariable Long id) {
        log.debug("REST request to get TypeProduit : {}", id);
        Optional<TypeProduitDTO> typeProduitDTO = typeProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeProduitDTO);
    }

    /**
     * {@code DELETE  /type-produits/:id} : delete the "id" typeProduit.
     *
     * @param id the id of the typeProduitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-produits/{id}")
    public ResponseEntity<Void> deleteTypeProduit(@PathVariable Long id) {
        log.debug("REST request to delete TypeProduit : {}", id);
        typeProduitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
