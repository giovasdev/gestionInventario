package com.inventory.service;

import com.inventory.model.Categoria;
import java.util.List;

public interface CategoriaService extends CrudService<Categoria, Integer> {
    // Métodos especificos para la gestión de categorías
    List<Categoria> findByNombre(String nombre) throws Exception;
    boolen existeNombre(String nombre) throws Exception;
}
