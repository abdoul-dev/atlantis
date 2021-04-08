package com.atlantis.bf.service;

import com.atlantis.bf.domain.LignesVentes;
import com.atlantis.bf.repository.LignesVentesRepository;
import com.atlantis.bf.service.dto.LignesVentesDTO;
import com.atlantis.bf.service.mapper.LignesVentesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LignesVentes}.
 */
@Service
@Transactional
public class LignesVentesService {

    private final Logger log = LoggerFactory.getLogger(LignesVentesService.class);

    private final LignesVentesRepository lignesVentesRepository;

    private final LignesVentesMapper lignesVentesMapper;

    public LignesVentesService(LignesVentesRepository lignesVentesRepository, LignesVentesMapper lignesVentesMapper) {
        this.lignesVentesRepository = lignesVentesRepository;
        this.lignesVentesMapper = lignesVentesMapper;
    }

    /**
     * Save a lignesVentes.
     *
     * @param lignesVentesDTO the entity to save.
     * @return the persisted entity.
     */
    public LignesVentesDTO save(LignesVentesDTO lignesVentesDTO) {
        log.debug("Request to save LignesVentes : {}", lignesVentesDTO);
        LignesVentes lignesVentes = lignesVentesMapper.toEntity(lignesVentesDTO);
        lignesVentes = lignesVentesRepository.save(lignesVentes);
        return lignesVentesMapper.toDto(lignesVentes);
    }

    /**
     * Get all the lignesVentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LignesVentesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LignesVentes");
        return lignesVentesRepository.findAll(pageable)
            .map(lignesVentesMapper::toDto);
    }


    /**
     * Get one lignesVentes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LignesVentesDTO> findOne(Long id) {
        log.debug("Request to get LignesVentes : {}", id);
        return lignesVentesRepository.findById(id)
            .map(lignesVentesMapper::toDto);
    }

    /**
     * Delete the lignesVentes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LignesVentes : {}", id);
        lignesVentesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LignesVentesDTO> findOrderItems(Long venteId) {
        log.debug("Request to get all Ues contain Ecues optionnelles");
        return lignesVentesRepository.findAll().stream().filter(lignesVentes -> venteId.equals(lignesVentes.getId())).map(lignesVentesMapper::toDto).collect(Collectors.toList());
    }
}
