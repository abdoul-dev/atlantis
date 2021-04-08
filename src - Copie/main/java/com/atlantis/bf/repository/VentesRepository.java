package com.atlantis.bf.repository;

import java.util.stream.Stream;

import com.atlantis.bf.domain.Ventes;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ventes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentesRepository extends JpaRepository<Ventes, Long>, JpaSpecificationExecutor<Ventes> {
    Stream<Ventes> findAllByAnnuleFalse();
}
