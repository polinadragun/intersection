package com.intersection.application.services.implementations;

import com.intersection.application.repositoryAbstractions.IListItemRepository;
import com.intersection.application.repositoryAbstractions.IListRepository;
import com.intersection.application.services.abstractions.IListService;
import com.intersection.application.services.resultType.Failure;
import com.intersection.application.services.resultType.IResultType;
import com.intersection.application.services.resultType.Success;
import com.intersection.domain.entity.List;
import com.intersection.domain.entity.ListItem;
import com.intersection.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListService implements IListService {
    private final IListRepository IListRepository;
    private final IListItemRepository IListItemRepository;

    @Autowired
    public ListService(IListRepository IListRepository, IListItemRepository IListItemRepository) {
        this.IListRepository = IListRepository;
        this.IListItemRepository = IListItemRepository;
    }

    @Override
    public IResultType<UUID> createList(String title, String description, User user) {
        if (title == null || title.isEmpty()) {
            return new Failure<>("Title cannot be null or empty");
        }

        List list = new List();
        list.setTitle(title);
        list.setDescription(description);
        list.setUser(user);
        list.setCreatedAt(LocalDateTime.now());
        list.setIsPublished(false);

        List savedList = IListRepository.save(list);
        return new Success<>("List created successfully", savedList.getId());
    }

    @Override
    public IResultType<List> getListByTitle(String title) {
        Optional<List> list = IListRepository.findByTitle(title);
        if (!list.isPresent()) {
            return new Failure<>("List not found");
        }

        return new Success<>("List found", list.get());
    }

    @Override
    public IResultType<Void> updateList(String title, String description) {
        if (title == null || title.isEmpty()) {
            return new Failure<>("Title cannot be null or empty");
        }

        Optional<List> optionalList = IListRepository.findByTitle(title);
        if (!optionalList.isPresent()) {
            return new Failure<>("List not found");
        }

        List list = optionalList.get();
        list.setTitle(title);
        list.setDescription(description);
        IListRepository.save(list);

        return new Success<>("List updated successfully");
    }

    @Override
    public IResultType<Void> deleteList(UUID id) {
        if (!IListRepository.findById(id).isPresent()) {
            return new Failure<>("List not found");
        }

        IListRepository.deleteById(id);
        return new Success<>("List deleted successfully");
    }

    @Override
    public IResultType<Void> publishList(UUID id) {
        Optional<List> optionalList = IListRepository.findById(id);
        if (!optionalList.isPresent()) {
            return new Failure<>("List not found");
        }

        List list = optionalList.get();
        if (list.getIsPublished()) {
            return new Failure<>("List is already published");
        }

        list.setIsPublished(true);
        IListRepository.save(list);
        return new Success<>("List published successfully");
    }

    @Override
    public IResultType<UUID> addItem(List list, String content) {
        if (content == null || content.isEmpty()) {
            return new Failure<>("Content cannot be null or empty");
        }

        ListItem listItem = new ListItem();
        listItem.setList(list);
        listItem.setContent(content);

        ListItem savedItem = IListItemRepository.save(listItem);
        return new Success<>("Item added successfully", savedItem.getId());
    }

    @Override
    public IResultType<Void> removeItem(UUID id) {
        if (!IListItemRepository.findById(id).isPresent()) {
            return new Failure<>("Item not found");
        }

        IListItemRepository.deleteById(id);
        return new Success<>("Item removed successfully");
    }

    @Override
    public IResultType<Collection<ListItem>> getItemsByListId(UUID listId) {
        if (!IListRepository.findById(listId).isPresent()) {
            return new Failure<>("List not found");
        }

        Collection<ListItem> items = IListItemRepository.findAllById(listId);
        return new Success<>("Items retrieved successfully", items);
    }
}
