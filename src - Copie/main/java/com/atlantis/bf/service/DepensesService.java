package com.atlantis.bf.service;

import com.atlantis.bf.domain.Depenses;
import com.atlantis.bf.repository.DepensesRepository;
import com.atlantis.bf.service.dto.DepensesDTO;
import com.atlantis.bf.service.mapper.DepensesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Depenses}.
 */
@Service
@Transactional
public class DepensesService {

    private final Logger log = LoggerFactory.getLogger(DepensesService.class);

    private final DepensesRepository depensesRepository;

    private final DepensesMapper depensesMapper;

    public DepensesService(DepensesRepository depensesRepository, DepensesMapper depensesMapper) {
        this.depensesRepository = depensesRepository;
        this.depensesMapper = depensesMapper;
    }

    /**
     * Save a depenses.
     *
     * @param depensesDTO the entity to save.
     * @return the persisted entity.
     */
    public DepensesDTO save(DepensesDTO depensesDTO) {
        log.debug("Request to save Depenses : {}", depensesDTO);
        Depenses depenses = depensesMapper.toEntity(depensesDTO);
        depenses = depensesRepository.save(depenses);
        return depensesMapper.toDto(depenses);
    }

    /**
     * Get all the depenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepensesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Depenses");
        return depensesRepository.findAll(pageable)
            .map(depensesMapper::toDto);
    }


    /**
     * Get one depenses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepensesDTO> findOne(Long id) {
        log.debug("Request to get Depenses : {}", id);
        return depensesRepository.findById(id)
            .map(depensesMapper::toDto);
    }

    /**
     * Delete the depenses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Depenses : {}", id);
        depensesRepository.deleteById(id);
    }
}
