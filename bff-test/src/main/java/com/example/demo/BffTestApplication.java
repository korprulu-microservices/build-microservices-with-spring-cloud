package com.example.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableCircuitBreaker
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class BffTestApplication {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/categoryNames")
    @HystrixCommand(fallbackMethod = "categoryNamesFallback")
    public List<String> categoryNames() {
        return restTemplate()
                .exchange("http://service-product/categories",
                        HttpMethod.GET,
                        RequestEntity.EMPTY,
                        new ParameterizedTypeReference<List<Category>>() {
                        })
                .getBody()
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }

    public List<String> categoryNamesFallback() {
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        SpringApplication.run(BffTestApplication.class, args);
    }
}

class Category {
    private Integer id;
    private String categoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
