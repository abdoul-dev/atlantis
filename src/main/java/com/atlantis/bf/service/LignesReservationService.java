package com.atlantis.bf.service;

import com.atlantis.bf.domain.LignesReservation;
import com.atlantis.bf.repository.LignesReservationRepository;
import com.atlantis.bf.service.dto.LignesReservationDTO;
import com.atlantis.bf.service.mapper.LignesReservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LignesReservation}.
 */
@Service
@Transactional
public class LignesReservationService {

    private final Logger log = LoggerFactory.getLogger(LignesReservationService.class);

    private final LignesReservationRepository lignesReservationRepository;

    private final LignesReservationMapper lignesReservationMapper;

    public LignesReservationService(LignesReservationRepository lignesReservationRepository, LignesReservationMapper lignesReservationMapper) {
        this.lignesReservationRepository = lignesReservationRepository;
        this.lignesReservationMapper = lignesReservationMapper;
    }

    /**
     * Save a lignesReservation.
     *
     * @param lignesReservationDTO the entity to save.
     * @return the persisted entity.
     */
    public LignesReservationDTO save(LignesReservationDTO lignesReservationDTO) {
        log.debug("Request to save LignesReservation : {}", lignesReservationDTO);
        LignesReservation lignesReservation = lignesReservationMapper.toEntity(lignesReservationDTO);
        lignesReservation = lignesReservationRepository.save(lignesReservation);
        return lignesReservationMapper.toDto(lignesReservation);
    }

    /**
     * Get all the lignesReservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LignesReservationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LignesReservations");
        return lignesReservationRepository.findAll(pageable)
            .map(lignesReservationMapper::toDto);
    }


    /**
     * Get one lignesReservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LignesReservationDTO> findOne(Long id) {
        log.debug("Request to get LignesReservation : {}", id);
        return lignesReservationRepository.findById(id)
            .map(lignesReservationMapper::toDto);
    }

    /**
     * Delete the lignesReservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LignesReservation : {}", id);
        lignesReservationRepository.deleteById(id);
    }
}
