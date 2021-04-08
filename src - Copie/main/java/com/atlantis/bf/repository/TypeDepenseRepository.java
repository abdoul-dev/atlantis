package com.atlantis.bf.repository;

import com.atlantis.bf.domain.TypeDepense;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TypeDepense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDepenseRepository extends JpaRepository<TypeDepense, Long>, JpaSpecificationExecutor<TypeDepense> {
}
