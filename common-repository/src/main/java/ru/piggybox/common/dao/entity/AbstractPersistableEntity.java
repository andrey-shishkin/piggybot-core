package ru.piggybox.common.dao.entity;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class AbstractPersistableEntity<K> implements Persistable<K>, IsNewSetter {

    @Transient
    protected boolean isNew;

    @Override
    public abstract K getId();

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
