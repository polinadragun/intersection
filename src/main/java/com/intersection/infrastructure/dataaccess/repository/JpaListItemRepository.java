package com.intersection.infrastructure.dataaccess.repository;

import com.intersection.application.repositoryAbstractions.ListItemRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.intersection.domain.entity.ListItem;

import java.util.Collection;
import java.util.UUID;

public interface JpaListItemRepository extends CrudRepository<ListItem, UUID>, ListItemRepository {
    @Override
    @Query("SELECT item FROM ListItem item WHERE item.listId = :listId")
    Collection<ListItem> findByListId(UUID listId);
}
