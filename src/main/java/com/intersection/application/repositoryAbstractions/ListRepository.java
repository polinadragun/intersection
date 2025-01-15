package com.intersection.application.repositoryAbstractions;

import com.intersection.domain.entity.List;

import java.util.Optional;
import java.util.UUID;

public interface ListRepository {
    List save(List list);

    Optional<List> findByTitle(String title);

    Boolean isFoundById(UUID id);

    Optional<List> findById(UUID id);

    void deleteById(UUID id);
}
