package ru.piggybox.common.dao.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericCrudRepository<K, T extends Persistable<K>> extends CrudRepository<T, K> {
}
