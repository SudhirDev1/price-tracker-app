package com.lestro.pricetracker.service;

import com.lestro.pricetracker.dto.TrackedProductDto;
import com.lestro.pricetracker.model.TrackedProduct;

import java.util.List;

public interface TrackedProductService {

    TrackedProduct addProduct(TrackedProductDto dto);

    TrackedProduct updateProduct(Long id, TrackedProductDto dto);

    TrackedProduct getProductById(Long id);

    List<TrackedProduct> getProductsByUser(Long userId);

    void deleteProduct(Long id);

    TrackedProduct refreshPrice(Long id); // will call ScrapingBee later
}