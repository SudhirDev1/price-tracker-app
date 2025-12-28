package com.lestro.pricetracker.controller;

import com.lestro.pricetracker.dto.TrackedProductDto;
import com.lestro.pricetracker.model.TrackedProduct;
import com.lestro.pricetracker.service.TrackedProductService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class TrackedProductController {

    private final TrackedProductService trackedProductService;

    public TrackedProductController(TrackedProductService trackedProductService) {
        this.trackedProductService = trackedProductService;
    }

    // ADD PRODUCT
    @PostMapping
    public ResponseEntity<TrackedProduct> addProduct(@Valid @RequestBody TrackedProductDto dto) {
        TrackedProduct created = trackedProductService.addProduct(dto);
        return ResponseEntity.ok(created);
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public ResponseEntity<TrackedProduct> updateProduct(
            @PathVariable Long id,
            @RequestBody TrackedProductDto dto
    ) {
        TrackedProduct updated = trackedProductService.updateProduct(id, dto);
        return ResponseEntity.ok(updated);
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TrackedProduct> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(trackedProductService.getProductById(id));
    }

    // GET PRODUCTS OF USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrackedProduct>> getProductsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(trackedProductService.getProductsByUser(userId));
    }

    // DELETE PRODUCT (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        trackedProductService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    // REFRESH PRICE
    @PostMapping("/{id}/refresh")
    public ResponseEntity<TrackedProduct> refreshPrice(@PathVariable Long id) {
        TrackedProduct refreshed = trackedProductService.refreshPrice(id);
        return ResponseEntity.ok(refreshed);
    }
}