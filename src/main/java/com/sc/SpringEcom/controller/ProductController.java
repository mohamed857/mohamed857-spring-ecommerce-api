package com.sc.SpringEcom.controller;

import com.sc.SpringEcom.model.Product;
import com.sc.SpringEcom.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() < 0) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() >= 0) {
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        Product savedProduct = null;
        try {
            savedProduct = productService.addOrUpdateProduct(product, image);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            LOGGER.error("Error while saving product: {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{productID}")
    public ResponseEntity<String> deleteProduct(@PathVariable int productId) {
        Product product = productService.getProductById(productId);
        if (product.getId() >= 0) {
            productService.deleteProduct(product);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/product")
    public ResponseEntity<String> updateProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        Product updateProduct = null;
        try {
            updateProduct = productService.addOrUpdateProduct(product, image);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        System.out.println("searching with :" + keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
