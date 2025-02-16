package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.model.Entity;

import java.util.List;

public abstract class AbstractRepository<T extends Entity> {

    abstract List<T> findAll();

    abstract void save(T entity);

    abstract void delete(T entity);

    abstract void update(T entity);
}
