package com.intersection.application.services.abstractions;

import java.util.Collection;
import java.util.UUID;
import com.intersection.domain.entity.List;
import com.intersection.domain.entity.ListItem;

public interface ListService {
    UUID createList(String title, String description, UUID userId);

    List getListByTitle(String title);

    void updateList(String title, String description);

    void deleteList(UUID id);

    void publishList(UUID id);

    UUID addItem(UUID listId, String content);

    void removeItem(UUID id);

    Collection<ListItem> getItemsByListId(UUID listId);
}
