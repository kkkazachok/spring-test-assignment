package com.guide.city_guide.repository;

import com.guide.city_guide.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    // Найти достопримечательности в радиусе от точки
    @Query(value = """
        SELECT a.*, 
               (6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) * 
               cos(radians(a.longitude) - radians(:lon)) + 
               sin(radians(:lat)) * sin(radians(a.latitude)))) AS distance
        FROM attractions a
        WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) * 
               cos(radians(a.longitude) - radians(:lon)) + 
               sin(radians(:lat)) * sin(radians(a.latitude)))) <= :radius
        ORDER BY distance
        """, nativeQuery = true)
    List<Attraction> findWithinRadius(@Param("lat") Double lat,
                                      @Param("lon") Double lon,
                                      @Param("radius") Double radius);

    // Фильтрация по категории
    @Query(value = """
        SELECT a.*, 
               (6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) * 
               cos(radians(a.longitude) - radians(:lon)) + 
               sin(radians(:lat)) * sin(radians(a.latitude)))) AS distance
        FROM attractions a
        WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) * 
               cos(radians(a.longitude) - radians(:lon)) + 
               sin(radians(:lat)) * sin(radians(a.latitude)))) <= :radius
        AND a.category = :category
        ORDER BY distance
        """, nativeQuery = true)
    List<Attraction> findWithinRadiusAndCategory(@Param("lat") Double lat,
                                                 @Param("lon") Double lon,
                                                 @Param("radius") Double radius,
                                                 @Param("category") String category);
}