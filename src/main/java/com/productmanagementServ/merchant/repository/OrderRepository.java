package com.productmanagementServ.merchant.repository;

import com.productmanagementServ.merchant.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);

    List<Order> findByStatusOrderByCreatedAtDesc(String status);

    @Query("SELECT o FROM Order o JOIN o.items i WHERE i.merchantId = :merchantId ORDER BY o.createdAt DESC")
    List<Order> findByMerchantId(@Param("merchantId") Integer merchantId);

    @Query("SELECT o FROM Order o JOIN o.items i WHERE i.merchantId = :merchantId AND o.status = :status ORDER BY o.createdAt DESC")
    List<Order> findByMerchantIdAndStatus(@Param("merchantId") Integer merchantId, @Param("status") String status);

    @Query("SELECT COUNT(DISTINCT o) FROM Order o JOIN o.items i WHERE i.merchantId = :merchantId")
    Long countByMerchantId(@Param("merchantId") Integer merchantId);

    @Query("SELECT COUNT(DISTINCT o) FROM Order o JOIN o.items i WHERE i.merchantId = :merchantId AND o.status = :status")
    Long countByMerchantIdAndStatus(@Param("merchantId") Integer merchantId, @Param("status") String status);
}