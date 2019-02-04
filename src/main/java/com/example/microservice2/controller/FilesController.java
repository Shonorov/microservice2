package com.example.microservice2.controller;

import com.example.microservice2.model.IndexedDocument;
import com.example.microservice2.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

@RestController
public class FilesController {

    @Autowired
    private FeignIndexService indexService;

    @Autowired
    private FilesRepository repository;

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public long addFile(@RequestParam("file") MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        long id = -1L;
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            id = repository.save(new IndexedDocument(convFile)).getId();
            indexService.indexContent(file.getBytes(), id);
        } catch (IOException e) {
            System.out.println("Upload failed!");
        }
        return id;
    }

    @GetMapping("/unindexed")
    public HashSet<IndexedDocument> getUnindexedDocuments() {
        return repository.findAllByIndexedIsFalse();
    }

    @PutMapping("/indexed")
    public HttpStatus setIndexed(@RequestParam("id") long id) {
        Optional<IndexedDocument> optional = repository.findById(id);
        HttpStatus response = HttpStatus.NOT_FOUND;
        if (optional.isPresent()) {
            IndexedDocument document = optional.get();
            document.setIndexed(true);
            repository.save(document);
            response = HttpStatus.OK;
        }
        return response;
    }
}
