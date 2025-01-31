package com.intersection.application.repositoryAbstractions;

import com.intersection.domain.entity.List;

import java.util.Optional;
import java.util.UUID;

public interface IListRepository {
    List save(List list);

    Optional<List> findByTitle(String title);

    Optional<List> findById(UUID id);

    void deleteById(UUID id);
}
