package org.example.currency_exchanger.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E> {

    Optional<E> findById(Long id);
    List<E> findAll();
    E save(E entity);
    void update(E entity);
    void delete(Long id);

}
