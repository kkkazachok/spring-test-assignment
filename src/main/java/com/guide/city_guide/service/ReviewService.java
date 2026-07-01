package com.guide.city_guide.service;

import com.guide.city_guide.dto.CreateReviewRequest;
import com.guide.city_guide.dto.ReviewDTO;
import com.guide.city_guide.entity.Attraction;
import com.guide.city_guide.entity.Review;
import com.guide.city_guide.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AttractionService attractionService;

    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request) {
        Attraction attraction = attractionService.getAttractionById(request.getAttractionId());

        Review review = new Review();
        review.setAttraction(attraction);
        review.setUserName(request.getUserName());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return convertToDTO(saved);
    }

    public List<ReviewDTO> getReviewsByAttraction(Long attractionId) {
        return reviewRepository.findByAttractionIdOrderByCreatedAtDesc(attractionId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserName(review.getUserName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dto;
    }
}