package com.melik.analytics.service.dataaccess.repository;

import java.util.Collection;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public interface AnalyticsCustomRepository<T, PK> {

    <S extends T> PK persist(S entity);

    <S extends T> void batchPersist(Collection<S> entities);

    <S extends T> S merge(S entity);

    <S extends T> void batchMerge(Collection<S> entities);

    void clear();
}
