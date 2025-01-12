package com.sc.SpringEcom.repo;

import com.sc.SpringEcom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query("SELECT p from Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(String keyword);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findProductsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = :quantity WHERE p.id = :productId")
    void updateStock(int productId, int quantity);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRating(int productId);

    Page<Product> findByCategory(String category, Pageable pageable);

//    @Query("SELECT p FROM Product p ORDER BY p.salesCount DESC")
//    Page<Product> findTopSellingProducts(Pageable pageable);
    // Sorting products by price or any other field
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);

}
