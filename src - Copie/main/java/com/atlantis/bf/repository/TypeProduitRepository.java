package com.atlantis.bf.repository;

import com.atlantis.bf.domain.TypeProduit;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TypeProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeProduitRepository extends JpaRepository<TypeProduit, Long>, JpaSpecificationExecutor<TypeProduit> {
}
