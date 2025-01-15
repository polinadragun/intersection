package com.intersection.domain.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "list_items")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "list_id", nullable = false)
    private UUID listId;

    @Column(name = "content", nullable = false)
    private String content;

    public UUID getId() { return this.id; }

    public void setListId(UUID id) { this.id = id; }

    public UUID getListId() { return this.listId; }

    public void setContent(String content) { this.content = content; }

    public String getContent() { return this.content; }
}
