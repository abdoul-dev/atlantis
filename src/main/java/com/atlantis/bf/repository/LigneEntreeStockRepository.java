package com.atlantis.bf.repository;

import java.util.List;
import java.util.stream.Stream;

import com.atlantis.bf.domain.LigneEntreeStock;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LigneEntreeStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneEntreeStockRepository extends JpaRepository<LigneEntreeStock, Long>, JpaSpecificationExecutor<LigneEntreeStock> {
    List<LigneEntreeStock> findByEntreestockId(Long entreestockId);
    List<LigneEntreeStock> findAll();

}
