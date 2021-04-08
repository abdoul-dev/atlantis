package com.atlantis.bf.repository;

import com.atlantis.bf.domain.LignesReservation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LignesReservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LignesReservationRepository extends JpaRepository<LignesReservation, Long>, JpaSpecificationExecutor<LignesReservation> {
}
