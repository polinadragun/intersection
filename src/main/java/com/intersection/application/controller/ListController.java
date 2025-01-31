package com.intersection.application.controller;

import com.intersection.application.elasticseach.entity.ListDocument;
import com.intersection.application.repositoryAbstractions.UserRepository;
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

    private final UserRepository userRepository;

    @Autowired
    public ListController(IListService listService, UserRepository userRepository) { this.listService = listService; this.userRepository = userRepository;}

    @PostMapping("/create")
    public ResponseEntity<?> createList(@RequestBody ListRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof org.springframework.security.core.userdetails.User)) {
            return new ResponseEntity<>("Unauthorized: Invalid principal type. Actual type: " + principal.getClass().getName(), HttpStatus.UNAUTHORIZED);
        }

        org.springframework.security.core.userdetails.User springSecurityUser = (org.springframework.security.core.userdetails.User) principal;

        User currentUser = userRepository.findByUsername(springSecurityUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        IResultType<UUID> result = listService.createList(request.getTitle(), request.getDescription(), currentUser);

        return result instanceof Success<UUID>
                ? new ResponseEntity<>(result.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(result.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{title}")
    public ResponseEntity<?> getListByTitle(@PathVariable("title") String title) {
        IResultType<List> rez = listService.getListByTitle(title);
        return rez instanceof Success<List>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateList(@PathVariable("id") UUID id, @RequestBody ListRequest request) {
        IResultType<Void> rez = listService.updateList(id, request.getTitle(), request.getDescription());
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteList(@PathVariable("id") UUID id) {
        IResultType<Void> rez = listService.deleteList(id);
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("publish/{id}")
    public ResponseEntity<?> publishList(@PathVariable("id") UUID id) {
        IResultType<Void> rez = listService.publishList(id);
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("add/item/{listId}")
    public ResponseEntity<?> addItem(@PathVariable("listId") UUID listId, @RequestParam String content) {
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

    @DeleteMapping("remove/item/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable("itemId") UUID itemId) {
        IResultType<Void> rez = listService.removeItem(itemId);
        return rez instanceof Success<Void>
                ? new ResponseEntity<>(rez.getMessage(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("get/items/{listId}")
    public ResponseEntity<?> getItemsByListId(@PathVariable("listId") UUID listId) {
        IResultType<Collection<ListItem>> rez = listService.getItemsByListId(listId);
        return rez instanceof Success<Collection<ListItem>>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchLists(@RequestParam String keyword) {
        IResultType<Collection<ListDocument>> rez = listService.searchLists(keyword);

        return rez instanceof Success<Collection<ListDocument>>
                ? new ResponseEntity<>(rez.getResult(), HttpStatus.OK)
                : new ResponseEntity<>(rez.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
