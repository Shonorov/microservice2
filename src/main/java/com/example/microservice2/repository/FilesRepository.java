package com.example.microservice2.repository;

import com.example.microservice2.model.IndexedDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface FilesRepository extends CrudRepository<IndexedDocument, Long> {
    HashSet<IndexedDocument> findAllByIndexedIsFalse();
}
