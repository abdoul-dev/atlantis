package com.atlantis.bf.service;

import com.atlantis.bf.domain.EntreeStock;
import com.atlantis.bf.repository.EntreeStockRepository;
import com.atlantis.bf.service.dto.EntreeStockDTO;
import com.atlantis.bf.service.mapper.EntreeStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EntreeStock}.
 */
@Service
@Transactional
public class EntreeStockService {

    private final Logger log = LoggerFactory.getLogger(EntreeStockService.class);

    private final EntreeStockRepository entreeStockRepository;

    private final EntreeStockMapper entreeStockMapper;

    public EntreeStockService(EntreeStockRepository entreeStockRepository, EntreeStockMapper entreeStockMapper) {
        this.entreeStockRepository = entreeStockRepository;
        this.entreeStockMapper = entreeStockMapper;
    }

    /**
     * Save a entreeStock.
     *
     * @param entreeStockDTO the entity to save.
     * @return the persisted entity.
     */
    public EntreeStockDTO save(EntreeStockDTO entreeStockDTO) {
        log.debug("Request to save EntreeStock : {}", entreeStockDTO);
        EntreeStock entreeStock = entreeStockMapper.toEntity(entreeStockDTO);
        entreeStock = entreeStockRepository.save(entreeStock);
        return entreeStockMapper.toDto(entreeStock);
    }

    /**
     * Get all the entreeStocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntreeStockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EntreeStocks");
        return entreeStockRepository.findAll(pageable)
            .map(entreeStockMapper::toDto);
    }


    /**
     * Get one entreeStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntreeStockDTO> findOne(Long id) {
        log.debug("Request to get EntreeStock : {}", id);
        return entreeStockRepository.findById(id)
            .map(entreeStockMapper::toDto);
    }

    /**
     * Delete the entreeStock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EntreeStock : {}", id);
        entreeStockRepository.deleteById(id);
    }
}
