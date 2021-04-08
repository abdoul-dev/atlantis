package com.atlantis.bf.repository;

import com.atlantis.bf.domain.Depenses;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Depenses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepensesRepository extends JpaRepository<Depenses, Long>, JpaSpecificationExecutor<Depenses> {
}
