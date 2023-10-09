package com.melik.analytics.service.dataaccess.repository;

import com.melik.analytics.service.dataaccess.entity.AnalyticsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, UUID>,
        AnalyticsCustomRepository<AnalyticsEntity, UUID> {

    @Query("Select analyticsEntity From AnalyticsEntity analyticsEntity where analyticsEntity.word=:word order by analyticsEntity.recordDate desc")
    List<AnalyticsEntity> getAnalyticsEntitiesByWord(@Param("word") String word, Pageable pageable);
}
