package com.guide.city_guide.service;

import com.guide.city_guide.dto.AttractionDTO;
import com.guide.city_guide.entity.Attraction;
import com.guide.city_guide.entity.Review;
import com.guide.city_guide.repository.AttractionRepository;
import com.guide.city_guide.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionRepository attractionRepository;
    private final ReviewRepository reviewRepository;

    // Найти достопримечательности в радиусе с фильтрацией
    public List<AttractionDTO> findNearby(Double lat, Double lon, Double radius,
                                          String category, Double minRating,
                                          int limit, String sortBy) {
        List<Attraction> attractions;

        if (category != null && !category.isEmpty()) {
            attractions = attractionRepository.findWithinRadiusAndCategory(lat, lon, radius, category);
        } else {
            attractions = attractionRepository.findWithinRadius(lat, lon, radius);
        }

        // Фильтр по рейтингу
        if (minRating != null) {
            attractions = attractions.stream()
                    .filter(a -> getAverageRating(a.getId()) >= minRating)
                    .collect(Collectors.toList());
        }

        // Сортировка и ограничение
        return attractions.stream()
                .limit(limit)
                .map(a -> convertToDTO(a, lat, lon))
                .collect(Collectors.toList());
    }

    public Attraction getAttractionById(Long id) {
        return attractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attraction not found with id: " + id));
    }

    public Double getAverageRating(Long attractionId) {
        Double avg = reviewRepository.getAverageRatingByAttractionId(attractionId);
        return avg != null ? avg : 0.0;
    }

    public AttractionDTO getAttractionInfo(Long id) {
        Attraction attraction = getAttractionById(id);
        return convertToDTO(attraction, null, null);
    }

    private AttractionDTO convertToDTO(Attraction attraction, Double userLat, Double userLon) {
        AttractionDTO dto = new AttractionDTO();
        dto.setId(attraction.getId());
        dto.setName(attraction.getName());
        dto.setCategory(attraction.getCategory());
        dto.setLatitude(attraction.getLatitude());
        dto.setLongitude(attraction.getLongitude());
        dto.setDescription(attraction.getDescription());
        dto.setAverageRating(getAverageRating(attraction.getId()));

        if (userLat != null && userLon != null) {
            double distance = calculateDistance(userLat, userLon,
                    attraction.getLatitude(), attraction.getLongitude());
            dto.setDistance(distance);
        }

        return dto;
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}