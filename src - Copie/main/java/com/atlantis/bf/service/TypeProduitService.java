package com.atlantis.bf.service;

import com.atlantis.bf.domain.TypeProduit;
import com.atlantis.bf.repository.TypeProduitRepository;
import com.atlantis.bf.service.dto.TypeProduitDTO;
import com.atlantis.bf.service.mapper.TypeProduitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TypeProduit}.
 */
@Service
@Transactional
public class TypeProduitService {

    private final Logger log = LoggerFactory.getLogger(TypeProduitService.class);

    private final TypeProduitRepository typeProduitRepository;

    private final TypeProduitMapper typeProduitMapper;

    public TypeProduitService(TypeProduitRepository typeProduitRepository, TypeProduitMapper typeProduitMapper) {
        this.typeProduitRepository = typeProduitRepository;
        this.typeProduitMapper = typeProduitMapper;
    }

    /**
     * Save a typeProduit.
     *
     * @param typeProduitDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeProduitDTO save(TypeProduitDTO typeProduitDTO) {
        log.debug("Request to save TypeProduit : {}", typeProduitDTO);
        TypeProduit typeProduit = typeProduitMapper.toEntity(typeProduitDTO);
        typeProduit = typeProduitRepository.save(typeProduit);
        return typeProduitMapper.toDto(typeProduit);
    }

    /**
     * Get all the typeProduits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeProduitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeProduits");
        return typeProduitRepository.findAll(pageable)
            .map(typeProduitMapper::toDto);
    }


    /**
     * Get one typeProduit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeProduitDTO> findOne(Long id) {
        log.debug("Request to get TypeProduit : {}", id);
        return typeProduitRepository.findById(id)
            .map(typeProduitMapper::toDto);
    }

    /**
     * Delete the typeProduit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeProduit : {}", id);
        typeProduitRepository.deleteById(id);
    }
}
