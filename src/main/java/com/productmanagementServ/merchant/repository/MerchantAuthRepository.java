package com.productmanagementServ.merchant.repository;

import com.productmanagementServ.merchant.entity.MerchantAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MerchantAuthRepository extends JpaRepository<MerchantAuth, Integer> {
    Optional<MerchantAuth> findByMerchantId(Integer merchantId);
}