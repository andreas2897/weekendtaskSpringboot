package com.example.weekendtask.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weekendtask.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
