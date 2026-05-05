package com.productmanagementServ.merchant.repository;

import com.productmanagementServ.merchant.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByProductId(Integer productId);

    @Query("SELECT i FROM Inventory i JOIN Product p ON i.productId = p.id WHERE p.merchantId = :merchantId")
    List<Inventory> findByMerchantId(@Param("merchantId") Integer merchantId);

    @Query("SELECT i FROM Inventory i JOIN Product p ON i.productId = p.id WHERE p.merchantId = :merchantId AND i.quantityOnHand <= i.lowStockThreshold")
    List<Inventory> findLowStockByMerchantId(@Param("merchantId") Integer merchantId);

    @Query("SELECT i FROM Inventory i JOIN Product p ON i.productId = p.id WHERE p.merchantId = :merchantId AND i.quantityOnHand = 0")
    List<Inventory> findOutOfStockByMerchantId(@Param("merchantId") Integer merchantId);

    @Query("SELECT COUNT(i) FROM Inventory i JOIN Product p ON i.productId = p.id WHERE p.merchantId = :merchantId AND i.quantityOnHand <= i.lowStockThreshold")
    Long countLowStockByMerchantId(@Param("merchantId") Integer merchantId);

    @Query("SELECT COUNT(i) FROM Inventory i JOIN Product p ON i.productId = p.id WHERE p.merchantId = :merchantId AND i.quantityOnHand = 0")
    Long countOutOfStockByMerchantId(@Param("merchantId") Integer merchantId);
}