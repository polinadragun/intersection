package com.intersection.application.elasticseach.repository;

import com.intersection.application.elasticseach.entity.ListDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ListDocumentRepository extends ElasticsearchRepository<ListDocument, UUID> {
}