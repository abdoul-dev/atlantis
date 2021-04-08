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

import com.atlantis.bf.domain.Depenses;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.DepensesRepository;
import com.atlantis.bf.service.dto.DepensesCriteria;
import com.atlantis.bf.service.dto.DepensesDTO;
import com.atlantis.bf.service.mapper.DepensesMapper;

/**
 * Service for executing complex queries for {@link Depenses} entities in the database.
 * The main input is a {@link DepensesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepensesDTO} or a {@link Page} of {@link DepensesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepensesQueryService extends QueryService<Depenses> {

    private final Logger log = LoggerFactory.getLogger(DepensesQueryService.class);

    private final DepensesRepository depensesRepository;

    private final DepensesMapper depensesMapper;

    public DepensesQueryService(DepensesRepository depensesRepository, DepensesMapper depensesMapper) {
        this.depensesRepository = depensesRepository;
        this.depensesMapper = depensesMapper;
    }

    /**
     * Return a {@link List} of {@link DepensesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepensesDTO> findByCriteria(DepensesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Depenses> specification = createSpecification(criteria);
        return depensesMapper.toDto(depensesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepensesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepensesDTO> findByCriteria(DepensesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Depenses> specification = createSpecification(criteria);
        return depensesRepository.findAll(specification, page)
            .map(depensesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepensesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Depenses> specification = createSpecification(criteria);
        return depensesRepository.count(specification);
    }

    /**
     * Function to convert {@link DepensesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Depenses> createSpecification(DepensesCriteria criteria) {
        Specification<Depenses> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Depenses_.id));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), Depenses_.montant));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), Depenses_.comments));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Depenses_.date));
            }
            if (criteria.getAnnule() != null) {
                specification = specification.and(buildSpecification(criteria.getAnnule(), Depenses_.annule));
            }
            if (criteria.getTypeDepenseId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeDepenseId(),
                    root -> root.join(Depenses_.typeDepense, JoinType.LEFT).get(TypeDepense_.id)));
            }
        }
        return specification;
    }
}
