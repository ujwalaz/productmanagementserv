package com.productmanagementServ.merchant.repository;

import com.productmanagementServ.merchant.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
}
