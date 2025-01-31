package com.intersection.domain.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "list_items")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private List list;

    @Column(name = "content", nullable = false)
    private String content;

    public UUID getId() { return this.id; }

    public void setList(List list) { this.list = list; }

    public List getList() { return this.list; }

    public void setContent(String content) { this.content = content; }

    public String getContent() { return this.content; }
}
