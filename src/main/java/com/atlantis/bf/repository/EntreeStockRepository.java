package com.atlantis.bf.repository;

import com.atlantis.bf.domain.EntreeStock;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EntreeStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntreeStockRepository extends JpaRepository<EntreeStock, Long>, JpaSpecificationExecutor<EntreeStock> {
}
