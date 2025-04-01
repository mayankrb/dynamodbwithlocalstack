package com.mayank.dynamodbwithlocalstack.controller;

import com.mayank.dynamodbwithlocalstack.model.Product;
import com.mayank.dynamodbwithlocalstack.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return null;
    }

    @GetMapping
    public List<Product> getAListOfProducts(@RequestBody List<String> productIds){
        return null;
    }

}
