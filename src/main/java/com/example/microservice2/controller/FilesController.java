package com.example.microservice2.controller;

import com.example.microservice2.model.IndexedDocument;
import com.example.microservice2.model.SearchResult;
import com.example.microservice2.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class FilesController {

    @Autowired
    private FeignIndexService indexService;

    @Autowired
    private FilesRepository repository;

    @GetMapping("/getByID")
    public byte[] getFilesByIDs(@RequestParam("id") long id) {
        Optional<IndexedDocument> optional = repository.findById(id);
        byte[] response = new byte[0];
        if (optional.isPresent()) {
            response = optional.get().getFile();
        }
        return response;
    }

    @GetMapping(value = "/getMonoByID", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> getMonoStringByID(@RequestParam("id") long id) {
        return Mono.fromCallable(() -> {
            Optional<IndexedDocument> optional = repository.findById(id);
            String result = "";
            if (optional.isPresent()) {
                result = new String(optional.get().getFile());
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(2000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        });
    }

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
            indexService.indexFileContent(file, id);
        } catch (IOException e) {
            System.out.println("Upload failed!");
        }
        return id;
    }

    @PostMapping("/addString")
    public long addFileAsString(@RequestParam("name") String name, @RequestParam("data") String text) {
        IndexedDocument document = new IndexedDocument(name, text);
        long id = repository.save(document).getId();
        indexService.indexContent(document.getFile(), id);
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
