package com.mayank.dynamodbwithlocalstack.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

/**
 * Configuration class for setting up DynamoDB client beans.
 * These clients are used to connect to LocalStack's DynamoDB during local development.
 */
@Configuration
public class DynamoDbConfig {

    /**
     * Creates a custom {@link DynamoDbEnhancedClient} bean.
     * This is a higher-level client from AWS SDK v2, which simplifies working with POJOs and object mapping.
     *
     * @param customDynamoDbClient the low-level DynamoDbClient to be wrapped by the enhanced client
     * @return a configured DynamoDbEnhancedClient
     */
    @Bean(name = "customDynamoDbEnhancedClient")
    public DynamoDbEnhancedClient customDynamoDbEnhancedClient(DynamoDbClient customDynamoDbClient) {
        System.out.println(">>> Creating custom DynamoDB Enhanced Client");
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(customDynamoDbClient)
                .build();
    }

    /**
     * Creates a custom {@link DynamoDbClient} bean configured for LocalStack.
     * This client connects to a local DynamoDB instance running in a Docker container.
     *
     * @param region    the AWS region to use (e.g., us-west-2)
     * @param endpoint  the local endpoint URL for DynamoDB (e.g., http://localhost:4566)
     * @param accessKey dummy access key for LocalStack
     * @param secretKey dummy secret key for LocalStack
     * @return a configured low-level DynamoDbClient
     */
    @Bean(name = "customDynamoDbClient")
    public DynamoDbClient customDynamoDbClient(
            @Value("${app.dynamodb.region}") String region,
            @Value("${app.dynamodb.endpoint}") String endpoint,
            @Value("${aws.accessKey}") String accessKey,
            @Value("${aws.secretKey}") String secretKey
    ) {
        System.out.println(">>> Creating custom DynamoDB Client");

        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .endpointOverride(URI.create(endpoint)) // Points to LocalStack instead of AWS cloud
                .build();
    }
}
