package com.codefreak.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codefreak.orderservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	
}
