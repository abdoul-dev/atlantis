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

import com.atlantis.bf.domain.LigneEntreeStock;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.LigneEntreeStockRepository;
import com.atlantis.bf.service.dto.LigneEntreeStockCriteria;
import com.atlantis.bf.service.dto.LigneEntreeStockDTO;
import com.atlantis.bf.service.mapper.LigneEntreeStockMapper;

/**
 * Service for executing complex queries for {@link LigneEntreeStock} entities in the database.
 * The main input is a {@link LigneEntreeStockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LigneEntreeStockDTO} or a {@link Page} of {@link LigneEntreeStockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LigneEntreeStockQueryService extends QueryService<LigneEntreeStock> {

    private final Logger log = LoggerFactory.getLogger(LigneEntreeStockQueryService.class);

    private final LigneEntreeStockRepository ligneEntreeStockRepository;

    private final LigneEntreeStockMapper ligneEntreeStockMapper;

    public LigneEntreeStockQueryService(LigneEntreeStockRepository ligneEntreeStockRepository, LigneEntreeStockMapper ligneEntreeStockMapper) {
        this.ligneEntreeStockRepository = ligneEntreeStockRepository;
        this.ligneEntreeStockMapper = ligneEntreeStockMapper;
    }

    /**
     * Return a {@link List} of {@link LigneEntreeStockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LigneEntreeStockDTO> findByCriteria(LigneEntreeStockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LigneEntreeStock> specification = createSpecification(criteria);
        return ligneEntreeStockMapper.toDto(ligneEntreeStockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LigneEntreeStockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneEntreeStockDTO> findByCriteria(LigneEntreeStockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LigneEntreeStock> specification = createSpecification(criteria);
        return ligneEntreeStockRepository.findAll(specification, page)
            .map(ligneEntreeStockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LigneEntreeStockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LigneEntreeStock> specification = createSpecification(criteria);
        return ligneEntreeStockRepository.count(specification);
    }

    /**
     * Function to convert {@link LigneEntreeStockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LigneEntreeStock> createSpecification(LigneEntreeStockCriteria criteria) {
        Specification<LigneEntreeStock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LigneEntreeStock_.id));
            }
            if (criteria.getPrixUnitaire() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixUnitaire(), LigneEntreeStock_.prixUnitaire));
            }
            if (criteria.getQuantite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantite(), LigneEntreeStock_.quantite));
            }
            if (criteria.getPrixTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixTotal(), LigneEntreeStock_.prixTotal));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductsId(),
                    root -> root.join(LigneEntreeStock_.products, JoinType.LEFT).get(Products_.id)));
            }
            if (criteria.getEntreestockId() != null) {
                specification = specification.and(buildSpecification(criteria.getEntreestockId(),
                    root -> root.join(LigneEntreeStock_.entreestock, JoinType.LEFT).get(EntreeStock_.id)));
            }
        }
        return specification;
    }
}
