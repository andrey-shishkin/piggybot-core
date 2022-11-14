package ru.piggybox.common.mapper;

import org.springframework.data.domain.Persistable;
import ru.piggybox.common.service.dao.Detail;

public interface GenericMapper<K, V extends Detail, T extends Persistable<K>> {

    T detailToEntity(K id, V detail);

    V entityToDetail(T entity);
}
