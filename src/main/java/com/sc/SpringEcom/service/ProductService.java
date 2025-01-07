package com.sc.SpringEcom.service;

import com.sc.SpringEcom.model.Product;
import com.sc.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
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
}
