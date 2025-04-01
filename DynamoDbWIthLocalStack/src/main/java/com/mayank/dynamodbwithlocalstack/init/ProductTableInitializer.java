package com.mayank.dynamodbwithlocalstack.init;


import com.mayank.dynamodbwithlocalstack.model.Product;
import com.mayank.dynamodbwithlocalstack.service.ProductDataDynamoDbService;
import com.mayank.dynamodbwithlocalstack.service.ProductDataExporterService;
import com.mayank.dynamodbwithlocalstack.service.ProductDataGeneratorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;



/**
 * Configuration class that initializes the DynamoDB 'Products' table on application startup.
 * If the table is empty, it generates fake product data, inserts it into DynamoDB, and exports it to JSON/CSV files.
 */
@Configuration
public class ProductTableInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ProductTableInitializer.class);

    // Service to generate fake product data
    private final ProductDataGeneratorService productDataGeneratorService;

    // Service to interact with DynamoDB
    private final ProductDataDynamoDbService productDataDynamoDbService;

    // Service to export product data to files
    private final ProductDataExporterService productDataExporterService;

    //inject the dependencies
    @Autowired
    public ProductTableInitializer(
            ProductDataGeneratorService productDataGeneratorService,
            ProductDataExporterService productDataExporterService,
            ProductDataDynamoDbService productDataDynamoDbService
    ){
        this.productDataGeneratorService = productDataGeneratorService;
        this.productDataDynamoDbService = productDataDynamoDbService;
        this.productDataExporterService = productDataExporterService;
    }

    // Name of the DynamoDB table
    private static final String TABLE_NAME = "Products";

    /**
     * Spring Boot ApplicationRunner bean that runs after the application context is initialized.
     * It checks if the Products table is empty. If so, it populates the table and exports the data.
     *
     * @return an ApplicationRunner instance to run initialization logic
     */
    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            // Check if the table already has data to avoid re-initialization
            if (!productDataDynamoDbService.isTableEmpty(TABLE_NAME)) {
                logger.info("Data already exists in table '{}'. Skipping initialization.", TABLE_NAME);
                return;
            }

            // Generate fake product data
            List<Product> products = productDataGeneratorService.generateDefault();

            // Insert the generated data into DynamoDB
            productDataDynamoDbService.insertProducts(products, TABLE_NAME);

            // Export the inserted data to JSON and CSV files
            productDataExporterService.export(products);
        };
    }
}

