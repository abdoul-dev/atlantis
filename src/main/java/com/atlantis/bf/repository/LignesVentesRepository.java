package com.atlantis.bf.repository;

import com.atlantis.bf.domain.LignesVentes;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LignesVentes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LignesVentesRepository extends JpaRepository<LignesVentes, Long>, JpaSpecificationExecutor<LignesVentes> {
}
