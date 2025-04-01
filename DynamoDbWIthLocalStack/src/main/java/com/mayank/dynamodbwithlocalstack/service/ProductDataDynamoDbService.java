package com.mayank.dynamodbwithlocalstack.service;

import com.mayank.dynamodbwithlocalstack.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for interacting with DynamoDB for Product-related operations.
 * Supports checking table status, batch insertion of products, and scanning table content.
 */
@Service
public class ProductDataDynamoDbService {

    private static final Logger logger = LoggerFactory.getLogger(ProductDataDynamoDbService.class);
    private static final int BATCH_SIZE = 25;
    private final DynamoDbClient dynamoDbClient;

    /**
     * Constructor injection of the custom DynamoDbClient bean.
     * @param dynamoDbClient The DynamoDbClient configured to point to LocalStack.
     */
    @Autowired
    public ProductDataDynamoDbService(@Qualifier("customDynamoDbClient") DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /**
     * Checks if a given DynamoDB table is empty by scanning only for a count of items.
     * @param tableName The name of the DynamoDB table.
     * @return true if the table is empty, false otherwise.
     */
    public boolean isTableEmpty(String tableName) {
        ScanResponse response = dynamoDbClient.scan(b -> b
                .tableName(tableName)
                .select(Select.COUNT) // Only count, no items fetched
                .limit(1)             // Optimize scan performance
        );
        return response.count() == 0;
    }

    /**
     * Inserts a list of Product objects into the DynamoDB table using batch operations.
     * @param products  List of products to be inserted.
     * @param tableName The DynamoDB table name.
     */
    public void insertProducts(List<Product> products, String tableName) {
        List<Product> batch = new ArrayList<>();

        for (Product p : products) {
            batch.add(p);

            // When batch reaches defined size, write it to DB
            if (batch.size() == BATCH_SIZE) {
                writeBatch(batch, tableName);
                batch.clear();
            }
        }

        // Handle the remaining batch if not empty
        if (!batch.isEmpty()) {
            writeBatch(batch, tableName);
        }

        logger.info("Inserted {} products into {}", products.size(), tableName);
    }

    /**
     * Writes a batch of products to the DynamoDB table.
     * Converts each Product object to a map of AttributeValues.
     * @param batch     List of products (max 25) to insert.
     * @param tableName DynamoDB table name.
     */
    private void writeBatch(List<Product> batch, String tableName) {
        List<WriteRequest> requests = new ArrayList<>();

        for (Product p : batch) {
            Map<String, AttributeValue> item = Map.of(
                    "id", AttributeValue.builder().s(p.getId()).build(),
                    "name", AttributeValue.builder().s(p.getName()).build(),
                    "description", AttributeValue.builder().s(p.getDescription()).build(),
                    "price", AttributeValue.builder().n(p.getPrice().toString()).build(),
                    "category", AttributeValue.builder().s(p.getCategory()).build()
            );

            requests.add(WriteRequest.builder()
                    .putRequest(PutRequest.builder().item(item).build())
                    .build());
        }

        BatchWriteItemRequest request = BatchWriteItemRequest.builder()
                .requestItems(Map.of(tableName, requests))
                .build();

        dynamoDbClient.batchWriteItem(request);
    }

    /**
     * Scans all items from the specified DynamoDB table.
     * (Note: This is resource-intensive and should not be used on large production tables.)
     * @param tableName The name of the table to scan.
     * @return A list of items as maps of AttributeValues.
     */
    public List<Map<String, AttributeValue>> scanTable(String tableName) {
        List<Map<String, AttributeValue>> allItems = new ArrayList<>();
        Map<String, AttributeValue> lastEvaluatedKey = null;
        ScanResponse response;

        do {
            // Scan the table with optional pagination
            response = dynamoDbClient.scan(ScanRequest.builder()
                    .tableName(tableName)
                    .exclusiveStartKey(lastEvaluatedKey)
                    .build());

            allItems.addAll(response.items());
            lastEvaluatedKey = response.lastEvaluatedKey();

        } while (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty());

        logger.info("Scanned {} items from {}", allItems.size(), tableName);
        return allItems;
    }
}