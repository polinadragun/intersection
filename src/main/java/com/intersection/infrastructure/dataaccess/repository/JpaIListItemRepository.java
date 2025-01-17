package com.intersection.infrastructure.dataaccess.repository;

import com.intersection.application.repositoryAbstractions.IListItemRepository;
import org.springframework.data.repository.CrudRepository;
import com.intersection.domain.entity.ListItem;

import java.util.UUID;

public interface JpaIListItemRepository extends CrudRepository<ListItem, UUID>, IListItemRepository {
}
