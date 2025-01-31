package com.intersection.application.elasticseach.entity;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.UUID;

@Document(indexName = "lists")
public class ListDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Boolean)
    private Boolean isPublished;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<ListItemDocument> items;


    public ListDocument(com.intersection.domain.entity.List list) {
        this.id = list.getId();
        this.title = list.getTitle();
        this.description = list.getDescription();
        this.isPublished = list.getIsPublished();
        this.items = list.getItems().stream()
                .map(ListItemDocument::new)
                .toList();
    }

    // Getters and Setters
}