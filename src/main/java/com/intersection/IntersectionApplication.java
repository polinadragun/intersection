package com.intersection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.intersection.application.elasticseach.repository")

public class IntersectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntersectionApplication.class, args);
    }

}
