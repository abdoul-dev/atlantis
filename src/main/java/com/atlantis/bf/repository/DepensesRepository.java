package com.atlantis.bf.repository;

import java.time.LocalDate;
import java.util.List;

import com.atlantis.bf.domain.Depenses;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Depenses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepensesRepository extends JpaRepository<Depenses, Long>, JpaSpecificationExecutor<Depenses> {
    List<Depenses> findBytypeDepenseIdAndDateBetween(Long typeDepenseId, LocalDate dateDebut, LocalDate dateFin);
}
