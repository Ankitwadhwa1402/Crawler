package com.example.crawler.service;

import com.example.crawler.model.CrawlResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CrawlerService {

    private static final List<String> PRODUCT_PATTERNS = Arrays.asList("/product/","products", "/item/", "/p/","category","/dp/", "/gp/product/",
            "/itm/","/ip/product","product-name");
    private static final int THREAD_POOL_SIZE = 2;

    public CrawlResult crawlDomains(List<String> domains) {
        CrawlerExecutor executorManager = new CrawlerExecutor(THREAD_POOL_SIZE);
        ExecutorService executorService = executorManager.getExecutorService();

        Map<String, List<String>> result = new HashMap<>();

        try {
            for (String domain : domains) {
                executorService.submit(() -> {
                    System.out.println("Crawling domain: " + domain + " on thread: " + Thread.currentThread().getName());
                    List<String> urls = crawlDomain(domain);
                    synchronized (urls) {
                        result.put(domain,urls);
                    }
                });
            }
        } finally {
            executorManager.shutdown();
        }

//        System.out.println("Crawling completed. Discovered URLs: " + result);

        CrawlResult crawlResult = new CrawlResult();
        crawlResult.setProductUrls(result);
        return crawlResult;
    }

    private List<String> crawlDomain(String domain) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<String> productUrls = new ArrayList<>();

        queue.add("http://" + domain);

        while (!queue.isEmpty()) {
            String url = queue.poll();
            if (visited.contains(url)) continue;

            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]");
                links.forEach(link -> {
                    String href = link.attr("abs:href");
                    if (!visited.contains(href)) {
                        if(productUrls.size()<50){
                            if (isProductUrl(href) ) {
                                productUrls.add(href);
                            } else if (href.contains(domain)) {
                                queue.add(href);
                            }
                        }
                    }
                });

                visited.add(url);
            } catch (Exception e) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }

        return productUrls;
    }

    private boolean isProductUrl(String url) {
        return PRODUCT_PATTERNS.stream().anyMatch(url::contains);
    }
}
