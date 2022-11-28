package ru.piggybox.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import ru.piggybox.common.dao.entity.IsNewSetter;
import ru.piggybox.common.dao.repository.GenericCrudRepository;
import ru.piggybox.common.exception.EntityDoesNotExistException;
import ru.piggybox.common.mapper.GenericMapper;
import ru.piggybox.common.service.dao.Detail;

public abstract class AbstractService<K, V extends Persistable<K> & IsNewSetter, T extends Detail,
        R extends GenericCrudRepository<K, V>,
        M extends GenericMapper<K, T, V>>
        implements GenericService<K, V, T, R, M> {

    @Autowired
    M mapper;
    @Autowired
    R repository;

    @Override
    public T create(K id, T detail) {
        V target = mapper.detailToEntity(id, detail);
        target.setNew(true);
        return mapper.entityToDetail(repository.save(target));
    }

    @Override
    public T getById(K id) throws EntityDoesNotExistException {
        return mapper.entityToDetail(repository.findById(id)
                .orElseThrow(EntityDoesNotExistException::new));
    }

    @Override
    public T update(K id, T detail) {
        V target = mapper.detailToEntity(id, detail);
        target.setNew(false);
        return mapper.entityToDetail(repository.save(target));
    }

    @Override
    public void delete(K id) {
        repository.deleteById(id);
    }

}
