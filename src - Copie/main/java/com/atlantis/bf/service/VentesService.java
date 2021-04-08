package com.atlantis.bf.service;

import com.atlantis.bf.domain.Ventes;
import com.atlantis.bf.repository.LignesVentesRepository;
import com.atlantis.bf.repository.VentesRepository;
import com.atlantis.bf.service.dto.VentesDTO;
import com.atlantis.bf.service.dto.LignesVentesDTO;
import com.atlantis.bf.service.mapper.LignesVentesMapper;
import com.atlantis.bf.service.mapper.VentesMapper;
import org.slf4j.Logger;
import java.util.List;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jdk.nashorn.internal.runtime.arrays.IntElements;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ventes}.
 */
@Service
@Transactional
public class VentesService {

    private final Logger log = LoggerFactory.getLogger(VentesService.class);

    private final VentesRepository ventesRepository;

    private final LignesVentesRepository lignesVentesRepository;

    private final VentesMapper ventesMapper;

    private final LignesVentesMapper lignesVentesMapper;

    public VentesService(VentesRepository ventesRepository, VentesMapper ventesMapper, LignesVentesRepository lignesVentesRepository, LignesVentesMapper lignesVentesMapper) {
        this.ventesRepository = ventesRepository;
        this.ventesMapper = ventesMapper;
        this.lignesVentesRepository = lignesVentesRepository;
        this.lignesVentesMapper = lignesVentesMapper;
    }

    /**
     * Save a ventes.
     *
     * @param ventesDTO the entity to save.
     * @return the persisted entity.
     */
    public VentesDTO save(VentesDTO ventesDTO) {
        log.debug("Request to save Ventes : {}", ventesDTO);
        Ventes ventes = ventesMapper.toEntity(ventesDTO);
        ventes = ventesRepository.save(ventes);
        return ventesMapper.toDto(ventes);
    }

    /**
     * Get all the ventes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VentesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ventes");
        return ventesRepository.findAll(pageable)
            .map(ventesMapper::toDto);
    }


    /**
     * Get one ventes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VentesDTO> findOne(Long id) {
        log.debug("Request to get Ventes : {}", id);
        return ventesRepository.findById(id)
            .map(ventesMapper::toDto);
    }


    /**
     * Delete the ventes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ventes : {}", id);
        ventesRepository.deleteById(id);
    }
}
