package com.intersection.application.services.implementations;

import com.intersection.application.repositoryAbstractions.ListItemRepository;
import com.intersection.application.repositoryAbstractions.ListRepository;
import com.intersection.application.services.abstractions.ListService;
import com.intersection.domain.entity.List;
import com.intersection.domain.entity.ListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
public class ListServiceImpl implements ListService {
    private final ListRepository listRepository;
    private final ListItemRepository listItemRepository;

    @Autowired
    public ListServiceImpl(ListRepository listRepository, ListItemRepository listItemRepository) {
        this.listRepository = listRepository;
        this.listItemRepository = listItemRepository;
    }

    @Override
    public UUID createList(String title, String description, UUID userId) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        List list = new List();
        list.setTitle(title);
        list.setDescription(description);
        list.setUserId(userId);
        list.setCreatedAt(LocalDateTime.now());
        list.setIsPublished(false);

        List savedList = listRepository.save(list);
        return savedList.getId();
    }

    @Override
    public List getListByTitle(String title) {
        return listRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("List not found"));
    }

    @Override
    public void updateList(String title, String description) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        List list = getListByTitle(title);

        list.setTitle(title);
        list.setDescription(description);
        listRepository.save(list);
    }

    @Override
    public void deleteList(UUID id) {
        if (!listRepository.isFoundById(id)) {
            throw new RuntimeException("List not found");
        }
        listRepository.deleteById(id);
    }

    @Override
    public void publishList(UUID id) {
        List list = listRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (list.getIsPublished()) {
            throw new RuntimeException("List is already published");
        }

        list.setIsPublished(true);
        listRepository.save(list);
    }

    @Override
    public UUID addItem(UUID listId, String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        ListItem listItem = new ListItem();
        listItem.setListId(listId);
        listItem.setContent(content);

        ListItem savedItem = listItemRepository.save(listItem);
        return savedItem.getId();
    }

    @Override
    public void removeItem(UUID id) {
        if (!listItemRepository.isFoundById(id)) {
            throw new RuntimeException("Item not found for ID: " + id);
        }

        listItemRepository.deleteById(id);
    }

    @Override
    public Collection<ListItem> getItemsByListId(UUID listId) {
        if (!listRepository.isFoundById(listId)) {
            throw new RuntimeException("List not found");
        }

        return listItemRepository.findByListId(listId);
    }
}
