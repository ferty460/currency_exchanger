package org.example.currency_exchanger.mapper;

// entity <-> dto
public interface EntityMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

}
