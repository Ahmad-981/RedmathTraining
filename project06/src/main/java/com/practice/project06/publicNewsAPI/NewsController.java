package com.practice.project06.publicNewsAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class NewsController {

    @Autowired
    private NewsApiClient newsApiClient;

    @GetMapping("/api/v1/all-news")
    public CompletableFuture<List<News>> getNews() {
        return newsApiClient.findNews();
    }
}

