package com.mayank.dynamodbwithlocalstack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayank.dynamodbwithlocalstack.model.Product;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Service responsible for exporting product data to external files.
 * Supports both JSON and CSV formats.
 */
@Service
public class ProductDataExporterService {

    /**
     * Orchestrates exporting to both JSON and CSV formats.
     * @param products List of products to export
     */
    public void export(List<Product> products) {
        exportToJson(products);
        exportToCsv(products);
    }

    /**
     * Serializes the list of Product objects to a JSON file.
     * Output file: products.json
     * @param products List of products to export
     */
    private void exportToJson(List<Product> products) {
        try {
            new ObjectMapper().writeValue(new File("products.json"), products);
            System.out.println("Exported to products.json");
        } catch (IOException e) {
            System.err.println("JSON export failed: " + e.getMessage());
        }
    }

    /**
     * Writes product data to a CSV file, escaping special characters for safety.
     * Output file: products.csv
     * @param products List of products to export
     */
    private void exportToCsv(List<Product> products) {
        try (FileWriter writer = new FileWriter("products.csv")) {
            // Write CSV header
            writer.write("id,name,description,price,category\n");

            for (Product p : products) {
                // Clean description to avoid breaking CSV format
                String description = p.getDescription().replaceAll("[\\r\\n]+", " ").replace(",", " ");
                String name = p.getName().replace(",", " ");

                writer.write(String.format("%s,%s,%s,%.2f,%s\n",
                        p.getId(), name, description, p.getPrice(), p.getCategory()));
            }

            System.out.println("Exported to products.csv");
        } catch (IOException e) {
            System.err.println("CSV export failed: " + e.getMessage());
        }
    }
}

