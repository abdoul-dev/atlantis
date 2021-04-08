package com.atlantis.bf.service;

import com.atlantis.bf.domain.TypeDepense;
import com.atlantis.bf.repository.TypeDepenseRepository;
import com.atlantis.bf.service.dto.TypeDepenseDTO;
import com.atlantis.bf.service.mapper.TypeDepenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TypeDepense}.
 */
@Service
@Transactional
public class TypeDepenseService {

    private final Logger log = LoggerFactory.getLogger(TypeDepenseService.class);

    private final TypeDepenseRepository typeDepenseRepository;

    private final TypeDepenseMapper typeDepenseMapper;

    public TypeDepenseService(TypeDepenseRepository typeDepenseRepository, TypeDepenseMapper typeDepenseMapper) {
        this.typeDepenseRepository = typeDepenseRepository;
        this.typeDepenseMapper = typeDepenseMapper;
    }

    /**
     * Save a typeDepense.
     *
     * @param typeDepenseDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDepenseDTO save(TypeDepenseDTO typeDepenseDTO) {
        log.debug("Request to save TypeDepense : {}", typeDepenseDTO);
        TypeDepense typeDepense = typeDepenseMapper.toEntity(typeDepenseDTO);
        typeDepense = typeDepenseRepository.save(typeDepense);
        return typeDepenseMapper.toDto(typeDepense);
    }

    /**
     * Get all the typeDepenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeDepenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeDepenses");
        return typeDepenseRepository.findAll(pageable)
            .map(typeDepenseMapper::toDto);
    }


    /**
     * Get one typeDepense by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeDepenseDTO> findOne(Long id) {
        log.debug("Request to get TypeDepense : {}", id);
        return typeDepenseRepository.findById(id)
            .map(typeDepenseMapper::toDto);
    }

    /**
     * Delete the typeDepense by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeDepense : {}", id);
        typeDepenseRepository.deleteById(id);
    }
}
