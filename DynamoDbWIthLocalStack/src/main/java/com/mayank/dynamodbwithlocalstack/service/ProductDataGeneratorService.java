package com.mayank.dynamodbwithlocalstack.service;

import com.github.javafaker.Faker;
import com.mayank.dynamodbwithlocalstack.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for generating fake product data using the Java Faker library.
 * This is useful for testing, seeding local databases, or simulating large datasets.
 */
@Service
public class ProductDataGeneratorService {

    // Default number of products to generate if no count is specified
    // For initial checking set it to 100
    private static final int DEFAULT_PRODUCT_COUNT = 100_000;

    // Predefined list of categories to randomly assign to products
    private static final String[] CATEGORIES = {
            "Electronics", "Clothing", "Books", "Food", "Furniture"
    };

    private final Faker faker = new Faker();

    /**
     * Generates a list of fake Product objects.
     *
     * @param count Number of products to generate
     * @return A list containing fake Product instances
     */
    public List<Product> generateProducts(int count) {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Product p = new Product(
                    UUID.randomUUID().toString(),                   // Unique ID
                    faker.commerce().productName(),                // Random product name
                    faker.lorem().sentence(15),                    // Random description
                    Double.parseDouble(faker.commerce().price(1.0, 500.0)), // Price between $1 and $500
                    CATEGORIES[faker.random().nextInt(CATEGORIES.length)]  // Random category
            );
            products.add(p);
        }

        return products;
    }

    /**
     * Generates a default number of fake products (100,000).
     *
     * @return A list of 100,000 fake Product instances
     */
    public List<Product> generateDefault() {
        return generateProducts(DEFAULT_PRODUCT_COUNT);
    }
}
