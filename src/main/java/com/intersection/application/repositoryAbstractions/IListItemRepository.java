package com.intersection.application.repositoryAbstractions;

import com.intersection.domain.entity.ListItem;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IListItemRepository {
    ListItem save(ListItem list);

    Optional<ListItem> findById(UUID id);

    Collection<ListItem> findAllById(UUID listId);

    void deleteById(UUID itemId);
}
