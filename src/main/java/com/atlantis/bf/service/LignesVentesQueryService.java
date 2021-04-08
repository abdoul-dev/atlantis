package com.atlantis.bf.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.atlantis.bf.domain.LignesVentes;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.LignesVentesRepository;
import com.atlantis.bf.service.dto.LignesVentesCriteria;
import com.atlantis.bf.service.dto.LignesVentesDTO;
import com.atlantis.bf.service.mapper.LignesVentesMapper;

/**
 * Service for executing complex queries for {@link LignesVentes} entities in the database.
 * The main input is a {@link LignesVentesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LignesVentesDTO} or a {@link Page} of {@link LignesVentesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LignesVentesQueryService extends QueryService<LignesVentes> {

    private final Logger log = LoggerFactory.getLogger(LignesVentesQueryService.class);

    private final LignesVentesRepository lignesVentesRepository;

    private final LignesVentesMapper lignesVentesMapper;

    public LignesVentesQueryService(LignesVentesRepository lignesVentesRepository, LignesVentesMapper lignesVentesMapper) {
        this.lignesVentesRepository = lignesVentesRepository;
        this.lignesVentesMapper = lignesVentesMapper;
    }

    /**
     * Return a {@link List} of {@link LignesVentesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LignesVentesDTO> findByCriteria(LignesVentesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LignesVentes> specification = createSpecification(criteria);
        return lignesVentesMapper.toDto(lignesVentesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LignesVentesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LignesVentesDTO> findByCriteria(LignesVentesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LignesVentes> specification = createSpecification(criteria);
        return lignesVentesRepository.findAll(specification, page)
            .map(lignesVentesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LignesVentesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LignesVentes> specification = createSpecification(criteria);
        return lignesVentesRepository.count(specification);
    }

    /**
     * Function to convert {@link LignesVentesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LignesVentes> createSpecification(LignesVentesCriteria criteria) {
        Specification<LignesVentes> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LignesVentes_.id));
            }
            if (criteria.getPrixUnitaire() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixUnitaire(), LignesVentes_.prixUnitaire));
            }
            if (criteria.getQuantite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantite(), LignesVentes_.quantite));
            }
            if (criteria.getPrixTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixTotal(), LignesVentes_.prixTotal));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductsId(),
                    root -> root.join(LignesVentes_.products, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getVentesId() != null) {
                specification = specification.and(buildSpecification(criteria.getVentesId(),
                    root -> root.join(LignesVentes_.ventes, JoinType.LEFT).get(Ventes_.id)));
            }
        }
        return specification;
    }
}
