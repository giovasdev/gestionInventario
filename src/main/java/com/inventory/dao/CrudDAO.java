package com.inventory.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO {
    /**
     * Guarda una nueva entidad
     * @param entity La entidad a guardar
     * @return la entidad guardada
     */
    T save(T entity) throws Exception;

    /**
     * Actualiza una entidad existente
     * @param entity La entidad a actualizar
     * @return la entidad actualizada
     */
    T update(T entity) throws Exception;

    /**
     * Elimina una entidad por su ID
     * @param id El ID de la entidad a eliminar
     */
    void delete(ID id) throws Exception;

    /**
     * Busca una entidad por su ID
     * @param id El ID de la entidad a buscar
     * @return Optional con la entidad encontrada
     */
    Optional<T> findById(ID id) throws Exception;

    /**
     * Obtiene todas las entidades
     * @return Lista de entidades
     */
    List<T> findAll() throws Exception;
}
