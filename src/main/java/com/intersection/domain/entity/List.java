package com.intersection.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UUID getId() { return this.id; }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return this.title; }

    public void setDescription(String description) { this.description = description; }

    public String getDescription() { return this.description; }

    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getUserId() { return this.userId; }

    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }

    public Boolean getIsPublished() { return this.isPublished; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }
}
