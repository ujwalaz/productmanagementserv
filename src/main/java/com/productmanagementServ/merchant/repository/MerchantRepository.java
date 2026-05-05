package com.productmanagementServ.merchant.repository;

import com.productmanagementServ.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {
    Optional<Merchant> findByPhone(String phone);
    boolean existsByPhone(String phone);
}