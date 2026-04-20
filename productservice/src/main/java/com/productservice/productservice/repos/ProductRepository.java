package com.productservice.productservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productservice.productservice.entities.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>,
        JpaSpecificationExecutor<Product> {
}
