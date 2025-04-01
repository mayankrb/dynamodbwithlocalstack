package com.mayank.dynamodbwithlocalstack.repository;

import com.mayank.dynamodbwithlocalstack.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
//    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
//    private final DynamoDbClient dynamoDbClient;
//    private final DynamoDbTable<Product> productTable;
//
//    @Autowired
//    public ProductRepository(@Qualifier("customDynamoDbEnhancedClient") DynamoDbEnhancedClient dynamoDbEnhancedClient, @Qualifier("customDynamoDbClient") DynamoDbClient dynamoDbClient) {
//        this.dynamoDbClient = dynamoDbClient;
//        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
//        this.productTable = dynamoDbEnhancedClient.table("Product", TableSchema.fromBean(Product.class));
//    }
//
//    public void saveProduct(Product product) {
//        productTable.putItem(product);
//    }
//
//    public Product getProductById(String id) {
//        Key key = Key.builder().partitionValue(id).build();
//        return productTable.getItem(key);
//    }
//
//    public void deleteProductById(String id) {
//        Key key = Key.builder().partitionValue(id).build();
//        productTable.deleteItem(key);
//    }
//
//    public List<Product> getAllProducts() {
//        return getAllProductsPaginated(1000);
//    }
//
//    public List<Product> getAllProductsPaginated(int pageSize) {
//        List<Product> allProducts = new ArrayList<>();
//
//        // Create a scan request with a page size
//        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
//                .limit(pageSize) // page size for each scan
//                .build();
//
//        // Iterate through pages
//        PageIterable<Product> pages = productTable.scan(scanRequest);
//        pages.stream()
//                .forEach(page -> {
//                    List<Product> items = page.items();
//                    allProducts.addAll(items);
//                    // Optional: log page info or process per page
//                });
//
//        return allProducts;
//    }
//
//
//    public void batchInsertProducts(List<Product> products) {
//        final int MAX_BATCH_SIZE = 25;
//        List<List<Product>> batches = partitionListIntoBatches(products, MAX_BATCH_SIZE);
//        for(List<Product> batch : batches) {
//            int attempt = 0;
//            Map<String, List<WriteRequest>> unprocessedItems = new HashMap<>();
//                do{
//                    BatchWriteItemResponse response =  writeBatchIntoProductTable(batch);
//                    unprocessedItems = response.unprocessedItems();
//
//                }while(!unprocessedItems.isEmpty());
//        }
//    }
//
//
//    private BatchWriteItemResponse writeBatchIntoProductTable(List<Product> batch){
//        List<WriteRequest> writeRequests = batch.stream()
//                .map(product -> WriteRequest.builder()
//                        .putRequest(PutRequest.builder()
//                                .item(productTable.tableSchema().itemToMap(product, true))
//                                .build())
//                        .build())
//                .collect(Collectors.toList());
//        Map<String, List<WriteRequest>> requestItems = new HashMap<>();
//        requestItems.put(this.productTable.tableName(), writeRequests);
//        BatchWriteItemRequest batchWriteItemRequest = BatchWriteItemRequest.builder()
//                .requestItems(requestItems).build();
//
//        return dynamoDbClient.batchWriteItem(batchWriteItemRequest);
//    }
//
//
//    private <T> List<List<T>> partitionListIntoBatches(List<T> list, int size) {
//        List<List<T>> partitions = new ArrayList<>();
//        for (int i = 0; i < list.size(); i += size) {
//            partitions.add(list.subList(i, Math.min(i + size, list.size())));
//        }
//        return partitions;
//    }
}
