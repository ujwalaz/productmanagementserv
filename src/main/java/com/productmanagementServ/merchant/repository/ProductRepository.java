package com.productmanagementServ.merchant.repository;

import com.productmanagementServ.merchant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByMerchantIdAndIsActiveTrue(Integer merchantId);

    Optional<Product> findByIdAndMerchantId(Integer id, Integer merchantId);

    @Query("SELECT p FROM Product p WHERE p.merchantId = :merchantId AND p.isActive = true " +
           "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByMerchantId(@Param("merchantId") Integer merchantId,
                                      @Param("searchTerm") String searchTerm);
    
    boolean existsBySkuAndMerchantId(String sku, Integer merchantId);
}