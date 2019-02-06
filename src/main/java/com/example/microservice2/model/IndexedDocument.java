package com.example.microservice2.model;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Entity(name = "indexed_documents")
public class IndexedDocument {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file", length = 100000)
    @Lob
    private byte[] file;

    @Column(name = "indexed")
    private boolean indexed;

    public IndexedDocument() {
    }

    public IndexedDocument(String fileName, String content) {
        this.fileName = fileName;
        this.file = content.getBytes();
        this.indexed = false;
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

    @Override
    public String toString() {
        return "IndexedDocument{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", file=" + Arrays.toString(file) +
                ", indexed=" + indexed +
                '}';
    }
}
