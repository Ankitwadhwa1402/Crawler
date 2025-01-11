package com.example.crawler.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CrawlRequest {
    private List<String> domains;
}