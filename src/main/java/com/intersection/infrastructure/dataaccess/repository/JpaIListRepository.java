package com.intersection.infrastructure.dataaccess.repository;

import com.intersection.application.repositoryAbstractions.IListRepository;
import com.intersection.domain.entity.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaIListRepository extends CrudRepository<List, UUID>, IListRepository {
    @Override
    @Query("SELECT l FROM List l WHERE l.title = :title")
    Optional<List> findByTitle(@Param("title") String title);
}
