package com.example.crawler.controller;

import com.example.crawler.model.CrawlRequest;
import com.example.crawler.model.CrawlResult;
import com.example.crawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @PostMapping("/start")
    public CrawlResult startCrawling(@RequestBody CrawlRequest request) throws InterruptedException {
        return crawlerService.crawlDomains(request.getDomains());
    }
}
