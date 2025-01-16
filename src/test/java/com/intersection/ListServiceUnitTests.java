package com.intersection;

import com.intersection.application.repositoryAbstractions.IListItemRepository;
import com.intersection.application.repositoryAbstractions.IListRepository;
import com.intersection.application.services.implementations.ListService;
import com.intersection.application.services.resultType.Failure;
import com.intersection.application.services.resultType.IResultType;
import com.intersection.application.services.resultType.Success;
import com.intersection.domain.entity.List;
import com.intersection.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListServiceUnitTests {
    @Mock
    private IListRepository IListRepository;

    @Mock
    private IListItemRepository IListItemRepository;

    @InjectMocks
    private ListService listService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateList_ReturnsSuccess() {
        // Arrange
        String title = "Test";
        String description = "Description";
        User user = new User();

        List savedList = new List();
        savedList.setTitle(title);
        savedList.setDescription(description);
        savedList.setUser(user);
        savedList.setCreatedAt(LocalDateTime.now());
        savedList.setIsPublished(false);

        when(IListRepository.save(any(List.class))).thenReturn(savedList);

        // Act
        IResultType<UUID> result = listService.createList(title, description, user);

        // Assert
        assertInstanceOf(Success.class, result);
        assertEquals("List created successfully", result.getMessage());

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(IListRepository).save(listCaptor.capture());
        assertEquals(title, listCaptor.getValue().getTitle());
    }

    @Test
    void testCreateList_TitleIsNull_ReturnsFailure() {
        // Arrange
        String title = null;
        String description = "Description";
        User user = new User();

        // Act
        IResultType<UUID> result = listService.createList(title, description, user);

        // Assert
        assertInstanceOf(Failure.class, result);
        assertEquals("Title cannot be null or empty", result.getMessage());
    }

    @Test
    void testGetListByTitle_ReturnsSuccess() {
        // Arrange
        String title = "Test";
        List list = new List();
        list.setTitle(title);

        when(IListRepository.findByTitle(title)).thenReturn(Optional.of(list));

        // Act
        IResultType<List> result = listService.getListByTitle(title);

        // Assert
        assertInstanceOf(Success.class, result);
        assertEquals(list, result.getResult());
        assertEquals("List found", result.getMessage());
    }

    @Test
    void testGetListByTitle_ListNotFound_ReturnsFailure() {
        // Arrange
        String title = "Nonexistent List";

        when(IListRepository.findByTitle(title)).thenReturn(Optional.empty());

        // Act
        IResultType<List> result = listService.getListByTitle(title);

        // Assert
        assertInstanceOf(Failure.class, result);
        assertEquals("List not found", result.getMessage());
    }

    @Test
    void testDeleteList_ReturnsSuccess() {
        // Arrange
        UUID listId = UUID.randomUUID();
        List list = new List();

        when(IListRepository.findById(listId)).thenReturn(Optional.of(list));

        // Act
        IResultType<Void> result = listService.deleteList(listId);

        // Assert
        assertInstanceOf(Success.class, result);
        assertEquals("List deleted successfully", result.getMessage());
        verify(IListRepository).deleteById(listId);
    }

    @Test
    void testDeleteList_ListNotFound_ReturnsFailure() {
        // Arrange
        UUID listId = UUID.randomUUID();

        when(IListRepository.findById(listId)).thenReturn(Optional.empty());

        // Act
        IResultType<Void> result = listService.deleteList(listId);

        // Assert
        assertInstanceOf(Failure.class, result);
        assertEquals("List not found", result.getMessage());
        verify(IListRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testPublishList_ReturnsSuccess() {
        // Arrange
        UUID listId = UUID.randomUUID();
        List list = new List();
        list.setIsPublished(false);

        when(IListRepository.findById(listId)).thenReturn(Optional.of(list));

        // Act
        IResultType<Void> result = listService.publishList(listId);

        // Assert
        assertInstanceOf(Success.class, result);
        assertEquals("List published successfully", result.getMessage());
        verify(IListRepository).save(list);
        assertTrue(list.getIsPublished());
    }

    @Test
    void testPublishList_ListAlreadyPublished_ReturnsFailure() {
        // Arrange
        UUID listId = UUID.randomUUID();
        List list = new List();
        list.setIsPublished(true);

        when(IListRepository.findById(listId)).thenReturn(Optional.of(list));

        // Act
        IResultType<Void> result = listService.publishList(listId);

        // Assert
        assertInstanceOf(Failure.class, result);
        assertEquals("List is already published", result.getMessage());
        verify(IListRepository, never()).save(any(List.class));
    }

    @Test
    void testAddItem_ContentIsEmpty_ReturnsFailure() {
        // Arrange
        List list = new List();
        String content = "";

        // Act
        IResultType<UUID> result = listService.addItem(list, content);

        // Assert
        assertInstanceOf(Failure.class, result);
        assertEquals("Content cannot be null or empty", result.getMessage());
    }
}
