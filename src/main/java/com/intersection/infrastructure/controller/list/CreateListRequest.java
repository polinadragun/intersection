package com.intersection.infrastructure.controller.list;

import java.util.UUID;

public class CreateListRequest {
    private String title;
    private String description;
//    private UUID userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public UUID getUserId() {
//        return userId;
//    }
//
//    public void setUserId(UUID userId) {
//        this.userId = userId;
//    }
}