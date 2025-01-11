package com.example.crawler.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CrawlResult {
    private Map<String, List<String>> productUrls;

}