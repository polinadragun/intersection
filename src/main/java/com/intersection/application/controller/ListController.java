package com.intersection.application.controller;

import com.intersection.application.services.abstractions.IListService;
import com.intersection.application.services.resultType.Failure;
import com.intersection.application.services.resultType.IResultType;
import com.intersection.application.services.resultType.Success;
import com.intersection.domain.entity.List;
import com.intersection.domain.entity.ListItem;
import com.intersection.domain.entity.User;
import com.intersection.application.controller.list.ListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/lists")
public class ListController {
    private final IListService listService;

    @Autowired
    public ListController(IListService listService) { this.listService = listService; }

    @PostMapping("/create")
    public ResponseEntity<?> createList(@RequestBody ListRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // var username = SecurityContextHolder.getContext().getAuthentication().getName();
        IResultType<UUID> rez = listService.createList(request.getTitle(), request.getDescription(), currentUser);

        return rez instanceof Success<UUID>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/lists/{title}")
    public ResponseEntity<?> getListByTitle(@PathVariable String title) {
        IResultType<List> rez = listService.getListByTitle(title);
        return rez instanceof Success<List>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateList(@PathVariable UUID id, @RequestBody ListRequest request) {
        IResultType<Void> rez = listService.updateList(request.getTitle(), request.getDescription());
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteList(@PathVariable UUID id) {
        IResultType<Void> rez = listService.deleteList(id);
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publishList(@PathVariable UUID id) {
        IResultType<Void> rez = listService.publishList(id);
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{listId}/items")
    public ResponseEntity<?> addItem(@PathVariable UUID listId, @RequestParam String content) {
        // ??? - в листе находимся
        IResultType<List> rezList = listService.getListById(listId);
        if (rezList instanceof Failure<List>) {
            return new ResponseEntity<>(rezList.getMessage(), HttpStatus.BAD_REQUEST);
        }

        List list = rezList.getResult();
        IResultType<UUID> rez = listService.addItem(list, content);
        return rez instanceof Success<UUID>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID itemId) {
        IResultType<Void> rez = listService.removeItem(itemId);
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{listId}/items")
    public ResponseEntity<?> getItemsByListId(@PathVariable UUID listId) {
        IResultType<Collection<ListItem>> rez = listService.getItemsByListId(listId);
        return rez instanceof Success<Collection<ListItem>>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
