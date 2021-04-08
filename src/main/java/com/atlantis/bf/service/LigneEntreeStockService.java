package com.atlantis.bf.service;

import com.atlantis.bf.domain.LigneEntreeStock;
import com.atlantis.bf.repository.LigneEntreeStockRepository;
import com.atlantis.bf.service.dto.LigneEntreeStockDTO;
import com.atlantis.bf.service.mapper.LigneEntreeStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing {@link LigneEntreeStock}.
 */
@Service
@Transactional
public class LigneEntreeStockService {

    private final Logger log = LoggerFactory.getLogger(LigneEntreeStockService.class);

    private final LigneEntreeStockRepository ligneEntreeStockRepository;

    private final LigneEntreeStockMapper ligneEntreeStockMapper;

    public LigneEntreeStockService(LigneEntreeStockRepository ligneEntreeStockRepository, LigneEntreeStockMapper ligneEntreeStockMapper) {
        this.ligneEntreeStockRepository = ligneEntreeStockRepository;
        this.ligneEntreeStockMapper = ligneEntreeStockMapper;
    }

    /**
     * Save a ligneEntreeStock.
     *
     * @param ligneEntreeStockDTO the entity to save.
     * @return the persisted entity.
     */
    public LigneEntreeStockDTO save(LigneEntreeStockDTO ligneEntreeStockDTO) {
        log.debug("Request to save LigneEntreeStock : {}", ligneEntreeStockDTO);
        LigneEntreeStock ligneEntreeStock = ligneEntreeStockMapper.toEntity(ligneEntreeStockDTO);
        ligneEntreeStock = ligneEntreeStockRepository.save(ligneEntreeStock);
        return ligneEntreeStockMapper.toDto(ligneEntreeStock);
    }

    /**
     * Get all the ligneEntreeStocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneEntreeStockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LigneEntreeStocks");
        return ligneEntreeStockRepository.findAll(pageable)
            .map(ligneEntreeStockMapper::toDto);
    }


    /**
     * Get one ligneEntreeStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LigneEntreeStockDTO> findOne(Long id) {
        log.debug("Request to get LigneEntreeStock : {}", id);
        return ligneEntreeStockRepository.findById(id)
            .map(ligneEntreeStockMapper::toDto);
    }

    /**
     * Delete the ligneEntreeStock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneEntreeStock : {}", id);
        ligneEntreeStockRepository.deleteById(id);
    }
}
