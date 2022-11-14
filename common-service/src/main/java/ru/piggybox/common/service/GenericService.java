package ru.piggybox.common.service;

import org.springframework.data.domain.Persistable;
import ru.piggybox.common.dao.entity.IsNewSetter;
import ru.piggybox.common.dao.repository.GenericCrudRepository;
import ru.piggybox.common.exception.EntityDoesNotExistException;
import ru.piggybox.common.mapper.GenericMapper;
import ru.piggybox.common.service.dao.Detail;

public interface GenericService<K, V extends Persistable<K> & IsNewSetter, T extends Detail, R extends GenericCrudRepository<K, V>,
        M extends GenericMapper<K, T, V>> {

    T create(K id, T detail);

    T getById(K id) throws EntityDoesNotExistException;

    T update(K id, T detail);

    void delete(K id);
}
