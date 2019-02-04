package com.example.microservice2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Entity(name = "indexed_documents")
public class IndexedDocument {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file")
    private byte[] file;

    @Column(name = "indexed")
    private boolean indexed;

    public IndexedDocument() {
    }

    public IndexedDocument(File file) {
        this.fileName = file.getName();
        try {
            this.file = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.indexed = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }
}
