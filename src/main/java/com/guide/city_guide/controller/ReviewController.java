package com.guide.city_guide.controller;

import com.guide.city_guide.dto.CreateReviewRequest;
import com.guide.city_guide.dto.ReviewDTO;
import com.guide.city_guide.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // POST /api/reviews
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody CreateReviewRequest request) {
        ReviewDTO review = reviewService.createReview(request);
        return ResponseEntity.ok(review);
    }

    // GET /api/reviews/attraction/{attractionId}
    @GetMapping("/attraction/{attractionId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByAttraction(@PathVariable Long attractionId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByAttraction(attractionId);
        return ResponseEntity.ok(reviews);
    }
}