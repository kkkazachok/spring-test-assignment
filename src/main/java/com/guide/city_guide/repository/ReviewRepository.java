package com.guide.city_guide.repository;

import com.guide.city_guide.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAttractionIdOrderByCreatedAtDesc(Long attractionId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.attraction.id = :attractionId")
    Double getAverageRatingByAttractionId(@Param("attractionId") Long attractionId);

    boolean existsByAttractionIdAndUserName(Long attractionId, String userName);
}