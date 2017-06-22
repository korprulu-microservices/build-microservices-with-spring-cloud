package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableFeignClients
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class BffTestApplication {

    @Autowired
    private ProductService productService;

    @GetMapping("/categoryNames")
    public List<String> categoryNames() {
        return productService.getCategories()
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        SpringApplication.run(BffTestApplication.class, args);
    }
}

@FeignClient(value = "service-product", fallback = ProductServiceFallback.class)
interface ProductService {
    @GetMapping("/categories")
    List<Category> getCategories();
}

@Component
class ProductServiceFallback implements ProductService {

    @Override
    public List<Category> getCategories() {
        return Collections.emptyList();
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
