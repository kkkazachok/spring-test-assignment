package com.guide.city_guide.controller;

import com.guide.city_guide.dto.AttractionDTO;
import com.guide.city_guide.service.AttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;

    // GET /api/attractions/nearby?lat=55.75&lon=37.61&radius=5&category=park&minRating=4&limit=10&sortBy=distance
    @GetMapping("/nearby")
    public ResponseEntity<List<AttractionDTO>> getNearbyAttractions(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "10") Double radius,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "distance") String sortBy) {

        List<AttractionDTO> attractions = attractionService.findNearby(
                lat, lon, radius, category, minRating, limit, sortBy);
        return ResponseEntity.ok(attractions);
    }

    // GET /api/attractions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AttractionDTO> getAttractionInfo(@PathVariable Long id) {
        AttractionDTO attraction = attractionService.getAttractionInfo(id);
        return ResponseEntity.ok(attraction);
    }
}