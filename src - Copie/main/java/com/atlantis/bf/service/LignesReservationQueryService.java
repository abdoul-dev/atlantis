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

import com.atlantis.bf.domain.LignesReservation;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.LignesReservationRepository;
import com.atlantis.bf.service.dto.LignesReservationCriteria;
import com.atlantis.bf.service.dto.LignesReservationDTO;
import com.atlantis.bf.service.mapper.LignesReservationMapper;

/**
 * Service for executing complex queries for {@link LignesReservation} entities in the database.
 * The main input is a {@link LignesReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LignesReservationDTO} or a {@link Page} of {@link LignesReservationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LignesReservationQueryService extends QueryService<LignesReservation> {

    private final Logger log = LoggerFactory.getLogger(LignesReservationQueryService.class);

    private final LignesReservationRepository lignesReservationRepository;

    private final LignesReservationMapper lignesReservationMapper;

    public LignesReservationQueryService(LignesReservationRepository lignesReservationRepository, LignesReservationMapper lignesReservationMapper) {
        this.lignesReservationRepository = lignesReservationRepository;
        this.lignesReservationMapper = lignesReservationMapper;
    }

    /**
     * Return a {@link List} of {@link LignesReservationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LignesReservationDTO> findByCriteria(LignesReservationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LignesReservation> specification = createSpecification(criteria);
        return lignesReservationMapper.toDto(lignesReservationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LignesReservationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LignesReservationDTO> findByCriteria(LignesReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LignesReservation> specification = createSpecification(criteria);
        return lignesReservationRepository.findAll(specification, page)
            .map(lignesReservationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LignesReservationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LignesReservation> specification = createSpecification(criteria);
        return lignesReservationRepository.count(specification);
    }

    /**
     * Function to convert {@link LignesReservationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LignesReservation> createSpecification(LignesReservationCriteria criteria) {
        Specification<LignesReservation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LignesReservation_.id));
            }
            if (criteria.getQuantite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantite(), LignesReservation_.quantite));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductsId(),
                    root -> root.join(LignesReservation_.products, JoinType.LEFT).get(Products_.id)));
            }
        }
        return specification;
    }
}
