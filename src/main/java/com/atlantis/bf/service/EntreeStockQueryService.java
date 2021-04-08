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

import com.atlantis.bf.domain.EntreeStock;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.EntreeStockRepository;
import com.atlantis.bf.service.dto.EntreeStockCriteria;
import com.atlantis.bf.service.dto.EntreeStockDTO;
import com.atlantis.bf.service.mapper.EntreeStockMapper;

/**
 * Service for executing complex queries for {@link EntreeStock} entities in the database.
 * The main input is a {@link EntreeStockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EntreeStockDTO} or a {@link Page} of {@link EntreeStockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntreeStockQueryService extends QueryService<EntreeStock> {

    private final Logger log = LoggerFactory.getLogger(EntreeStockQueryService.class);

    private final EntreeStockRepository entreeStockRepository;

    private final EntreeStockMapper entreeStockMapper;

    public EntreeStockQueryService(EntreeStockRepository entreeStockRepository, EntreeStockMapper entreeStockMapper) {
        this.entreeStockRepository = entreeStockRepository;
        this.entreeStockMapper = entreeStockMapper;
    }

    /**
     * Return a {@link List} of {@link EntreeStockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EntreeStockDTO> findByCriteria(EntreeStockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EntreeStock> specification = createSpecification(criteria);
        return entreeStockMapper.toDto(entreeStockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EntreeStockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EntreeStockDTO> findByCriteria(EntreeStockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EntreeStock> specification = createSpecification(criteria);
        return entreeStockRepository.findAll(specification, page)
            .map(entreeStockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntreeStockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EntreeStock> specification = createSpecification(criteria);
        return entreeStockRepository.count(specification);
    }

    /**
     * Function to convert {@link EntreeStockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EntreeStock> createSpecification(EntreeStockCriteria criteria) {
        Specification<EntreeStock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EntreeStock_.id));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), EntreeStock_.montant));
            }
            if (criteria.getAnnule() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnule(), EntreeStock_.annule));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), EntreeStock_.date));
            }
            if (criteria.getLigneEntreeStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getLigneEntreeStockId(),
                    root -> root.join(EntreeStock_.ligneEntreeStocks, JoinType.LEFT).get(LigneEntreeStock_.id)));
            }
            if (criteria.getFournisseurId() != null) {
                specification = specification.and(buildSpecification(criteria.getFournisseurId(),
                    root -> root.join(EntreeStock_.fournisseur, JoinType.LEFT).get(Fournisseur_.id)));
            }
        }
        return specification;
    }
}
