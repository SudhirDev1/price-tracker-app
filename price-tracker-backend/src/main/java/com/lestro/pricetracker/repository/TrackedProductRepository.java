package com.lestro.pricetracker.repository;

import com.lestro.pricetracker.model.TrackedProduct;
import com.lestro.pricetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackedProductRepository extends JpaRepository<TrackedProduct, Long> {

    List<TrackedProduct> findByUserAndIsActiveTrue(User user);

    List<TrackedProduct> findByIsActiveTrue();

}