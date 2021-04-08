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

import com.atlantis.bf.domain.TypeProduit;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.TypeProduitRepository;
import com.atlantis.bf.service.dto.TypeProduitCriteria;
import com.atlantis.bf.service.dto.TypeProduitDTO;
import com.atlantis.bf.service.mapper.TypeProduitMapper;

/**
 * Service for executing complex queries for {@link TypeProduit} entities in the database.
 * The main input is a {@link TypeProduitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeProduitDTO} or a {@link Page} of {@link TypeProduitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeProduitQueryService extends QueryService<TypeProduit> {

    private final Logger log = LoggerFactory.getLogger(TypeProduitQueryService.class);

    private final TypeProduitRepository typeProduitRepository;

    private final TypeProduitMapper typeProduitMapper;

    public TypeProduitQueryService(TypeProduitRepository typeProduitRepository, TypeProduitMapper typeProduitMapper) {
        this.typeProduitRepository = typeProduitRepository;
        this.typeProduitMapper = typeProduitMapper;
    }

    /**
     * Return a {@link List} of {@link TypeProduitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeProduitDTO> findByCriteria(TypeProduitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeProduit> specification = createSpecification(criteria);
        return typeProduitMapper.toDto(typeProduitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypeProduitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeProduitDTO> findByCriteria(TypeProduitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeProduit> specification = createSpecification(criteria);
        return typeProduitRepository.findAll(specification, page)
            .map(typeProduitMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeProduitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeProduit> specification = createSpecification(criteria);
        return typeProduitRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeProduitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeProduit> createSpecification(TypeProduitCriteria criteria) {
        Specification<TypeProduit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeProduit_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), TypeProduit_.libelle));
            }
            if (criteria.getIsDisabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDisabled(), TypeProduit_.isDisabled));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductsId(),
                    root -> root.join(TypeProduit_.products, JoinType.LEFT).get(Products_.id)));
            }
        }
        return specification;
    }
}
