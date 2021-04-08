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

import com.atlantis.bf.domain.Products;
import com.atlantis.bf.domain.*; // for static metamodels
import com.atlantis.bf.repository.ProductsRepository;
import com.atlantis.bf.service.dto.ProductsCriteria;
import com.atlantis.bf.service.dto.ProductsDTO;
import com.atlantis.bf.service.mapper.ProductsMapper;

/**
 * Service for executing complex queries for {@link Products} entities in the database.
 * The main input is a {@link ProductsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductsDTO} or a {@link Page} of {@link ProductsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductsQueryService extends QueryService<Products> {

    private final Logger log = LoggerFactory.getLogger(ProductsQueryService.class);

    private final ProductsRepository productsRepository;

    private final ProductsMapper productsMapper;

    public ProductsQueryService(ProductsRepository productsRepository, ProductsMapper productsMapper) {
        this.productsRepository = productsRepository;
        this.productsMapper = productsMapper;
    }

    /**
     * Return a {@link List} of {@link ProductsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductsDTO> findByCriteria(ProductsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Products> specification = createSpecification(criteria);
        return productsMapper.toDto(productsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductsDTO> findByCriteria(ProductsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.findAll(specification, page)
            .map(productsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Products> specification = createSpecification(criteria);
        return productsRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Products> createSpecification(ProductsCriteria criteria) {
        Specification<Products> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Products_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Products_.libelle));
            }
            if (criteria.getPrixVente() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixVente(), Products_.prixVente));
            }
            if (criteria.getIsDisabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDisabled(), Products_.isDisabled));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), Products_.comments));
            }
            if (criteria.getLigneStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getLigneStockId(),
                    root -> root.join(Products_.ligneStocks, JoinType.LEFT).get(LigneEntreeStock_.id)));
            }
            if (criteria.getLigneVentesId() != null) {
                specification = specification.and(buildSpecification(criteria.getLigneVentesId(),
                    root -> root.join(Products_.ligneVentes, JoinType.LEFT).get(LignesVentes_.id)));
            }
            if (criteria.getLigneReservationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLigneReservationId(),
                    root -> root.join(Products_.ligneReservations, JoinType.LEFT).get(LignesReservation_.id)));
            }
            if (criteria.getTypeProduitId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeProduitId(),
                    root -> root.join(Products_.typeProduit, JoinType.LEFT).get(TypeProduit_.id)));
            }
        }
        return specification;
    }
}
