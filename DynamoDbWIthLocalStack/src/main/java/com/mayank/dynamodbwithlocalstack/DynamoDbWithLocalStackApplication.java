package com.mayank.dynamodbwithlocalstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


@SpringBootApplication
public class DynamoDbWithLocalStackApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamoDbWithLocalStackApplication.class, args);
    }
}
