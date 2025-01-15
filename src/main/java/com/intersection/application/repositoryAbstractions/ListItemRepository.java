package com.intersection.application.repositoryAbstractions;

import com.intersection.domain.entity.ListItem;

import java.util.Collection;
import java.util.UUID;

public interface ListItemRepository {
    ListItem save(ListItem list);

    Boolean isFoundById(UUID id);

    Collection<ListItem> findByListId(UUID listId);

    void deleteById(UUID itemId);
}
