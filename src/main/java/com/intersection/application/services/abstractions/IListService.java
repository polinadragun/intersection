package com.intersection.application.services.abstractions;

import java.util.Collection;
import java.util.UUID;

import com.intersection.application.elasticseach.entity.ListDocument;
import com.intersection.application.services.resultType.IResultType;
import com.intersection.domain.entity.List;
import com.intersection.domain.entity.ListItem;
import com.intersection.domain.entity.User;

public interface IListService {
    IResultType<UUID> createList(String title, String description, User user);

    IResultType<List> getListByTitle(String title);

    IResultType<List> getListById(UUID listId);

    IResultType<Void> updateList(UUID id, String newTitle, String newDescription);

    IResultType<Void> deleteList(UUID id);

    IResultType<Void> publishList(UUID id);

    IResultType<UUID> addItem(List list, String content);

    IResultType<Void> removeItem(UUID id);

    IResultType<Collection<ListItem>> getItemsByListId(UUID listId);
    IResultType<Collection<ListDocument>> searchLists(String keyword);

}
