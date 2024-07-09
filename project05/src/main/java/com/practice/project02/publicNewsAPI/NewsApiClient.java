package com.practice.project02.publicNewsAPI;

import org.slf4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class NewsApiClient {

    private final RestTemplate restTemplate;
    private static final String API_KEY = "https://gnews.io/api/v4/top-headlines?country=pk&category=general&apikey=dc4cc304612b90d1d29f3bbe742dd1d9" ;
    private static final String API_URL = API_KEY;

    public NewsApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Cacheable(cacheNames = "news")
    @Async
    public CompletableFuture<List<News>> findNews() {
        NewsResponse response = restTemplate.getForObject(API_URL, NewsResponse.class);
        if(response == null){
            return CompletableFuture.completedFuture(List.of());
        }
        else {
            return CompletableFuture.completedFuture(response.getArticles());
        }

    }

    @Scheduled(fixedRate = 100000)
    public void fetchNewsRegularly() {
        findNews();
    }
}
