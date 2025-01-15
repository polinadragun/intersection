package com.intersection.infrastructure.dataaccess.repository;

import com.intersection.application.repositoryAbstractions.ListRepository;
import com.intersection.domain.entity.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaListRepository extends CrudRepository<List, UUID>, ListRepository {
    @Override
    @Query("SELECT l FROM List l WHERE l.title = :title")
    Optional<List> findByTitle(@Param("title") String title);

    @Override
    @Query("SELECT l from List l WHERE l.id = :id")
    Optional<List> findById(@Param("id") UUID id);
}
