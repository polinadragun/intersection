package com.intersection.application.elasticseach.entity;

import com.intersection.domain.entity.ListItem;
import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Document(indexName = "list_items")
public class ListItemDocument {

    @Id
    private UUID id;

    @Field(type = FieldType.Keyword)
    private UUID listId;

    @Field(type = FieldType.Text)
    private String content;

    public ListItemDocument() {}

    public ListItemDocument(ListItem listItem) {
        this.id = listItem.getId();
        this.listId = listItem.getList().getId();
        this.content = listItem.getContent();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getListId() { return listId; }
    public void setListId(UUID listId) { this.listId = listId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}