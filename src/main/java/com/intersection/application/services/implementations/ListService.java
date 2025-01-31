package com.intersection.application.services.implementations;

import com.intersection.application.elasticseach.entity.ListDocument;
import com.intersection.application.elasticseach.repository.ListDocumentRepository;
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
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.query.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListService implements IListService {
    private final IListRepository listRepository;
    private final IListItemRepository listItemRepository;

    private final ListDocumentRepository listDocumentRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ListService(IListRepository listRepository, IListItemRepository listItemRepository,
                       ListDocumentRepository listDocumentRepository, ElasticsearchOperations elasticsearchOperations) {
        this.listRepository = listRepository;
        this.listItemRepository = listItemRepository;
        this.listDocumentRepository = listDocumentRepository;
        this.elasticsearchOperations = elasticsearchOperations;
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

        List savedList = listRepository.save(list);

        ListDocument listDocument = new ListDocument(savedList);
        listDocumentRepository.save(listDocument);
        return new Success<>("List created successfully", savedList.getId());
    }

    @Override
    public IResultType<List> getListByTitle(String title) {
        Optional<List> list = listRepository.findByTitle(title);
        if (!list.isPresent()) {
            return new Failure<>("List not found");
        }

        return new Success<>("List found", list.get());
    }

    @Override
    public IResultType<List> getListById(UUID listId) {
        Optional<List> list = listRepository.findById(listId);
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

        Optional<List> optionalList = listRepository.findByTitle(title);
        if (!optionalList.isPresent()) {
            return new Failure<>("List not found");
        }

        List list = optionalList.get();
        list.setTitle(title);
        list.setDescription(description);
        listRepository.save(list);

        return new Success<>("List updated successfully");
    }

    @Override
    public IResultType<Void> deleteList(UUID id) {
        if (!listRepository.findById(id).isPresent()) {
            return new Failure<>("List not found");
        }

        listRepository.deleteById(id);
        return new Success<>("List deleted successfully");
    }

    @Override
    public IResultType<Void> publishList(UUID id) {
        Optional<List> optionalList = listRepository.findById(id);
        if (!optionalList.isPresent()) {
            return new Failure<>("List not found");
        }

        List list = optionalList.get();
        if (list.getIsPublished()) {
            return new Failure<>("List is already published");
        }

        list.setIsPublished(true);
        listRepository.save(list);
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

        ListItem savedItem = listItemRepository.save(listItem);

        Optional<List> updatedList = listRepository.findById(list.getId());
        updatedList.ifPresent(l -> {
            ListDocument listDocument = new ListDocument(l);
            listDocumentRepository.save(listDocument);
        });
        return new Success<>("Item added successfully", savedItem.getId());
    }

    @Override
    public IResultType<Void> removeItem(UUID id) {
        if (!listItemRepository.findById(id).isPresent()) {
            return new Failure<>("Item not found");
        }

        listItemRepository.deleteById(id);
        return new Success<>("Item removed successfully");
    }

    @Override
    public IResultType<Collection<ListItem>> getItemsByListId(UUID listId) {
        if (!listRepository.findById(listId).isPresent()) {
            return new Failure<>("List not found");
        }

        Collection<ListItem> items = listItemRepository.findAllById(listId);
        return new Success<>("Items retrieved successfully", items);
    }
    @Override
    public IResultType<Collection<ListDocument>> searchLists(String keyword) {
        Criteria criteria = new Criteria("title").matches(keyword)
                .or(new Criteria("description").matches(keyword))
                .or(new Criteria("items.content").matches(keyword));

        Query searchQuery = new CriteriaQuery(criteria);

        SearchHits<ListDocument> results = elasticsearchOperations.search(searchQuery, ListDocument.class);

        Collection<ListDocument> documents = results.stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        return new Success<>("Search results", documents);
    }
}
