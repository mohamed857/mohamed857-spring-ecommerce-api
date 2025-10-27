package com.sc.SpringEcom.service;

import com.sc.SpringEcom.model.Product;
import com.sc.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    public Product getProductById(int id) {
        return (Product) productRepo.findById(id).orElse(new Product(-1));
    }

    public void addProduct(Product product) {
         productRepo.save(product);
    }


    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        return (Product) productRepo.save(product);
    }
    public void deleteProduct(Product product){
        productRepo.delete(product);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }

    public Page<Product> filterProductsByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepo.findProductsByPriceRange(minPrice, maxPrice, pageable);
    }
    public void updateStock(int productId, int quantity) {
        productRepo.updateStock(productId, quantity);
    }
//    public Double getAverageRating(int productId) {
//        return productRepo.getAverageRating(productId);
//    }

    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepo.findByCategory(category, pageable);
    }
//    public Page<Product> getTopSellingProducts(Pageable pageable) {
//        return productRepo.findTopSellingProducts(pageable);
//    }
    public Page<Product> sortProducts(Pageable pageable, boolean ascending) {
        if (ascending) {
            return productRepo.findAllByOrderByPriceAsc(pageable);
        } else {
            return productRepo.findAllByOrderByPriceDesc(pageable);
        }
    }



}
