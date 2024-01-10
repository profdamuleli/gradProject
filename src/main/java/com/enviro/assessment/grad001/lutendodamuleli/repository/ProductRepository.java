package com.enviro.assessment.grad001.lutendodamuleli.repository;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
