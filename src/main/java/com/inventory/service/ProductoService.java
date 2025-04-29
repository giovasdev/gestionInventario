package com.inventory.service;

import com.inventory.model.Producto;
import java.util.List;

public interface ProductoService extends CrudService<Producto, Integer> {
    // Métodos específicos para la gestión de productos
    List<Producto> findByCategoria(Integer idCategoria) throws Exception;
    List<Producto> findByProveedor(Integer idProveedor) throws Exception;
    List<Producto> findByNombre(String nombre) throws Exception;
    Producto findByCodigo(String codigo) throws Exception;
    boolean existeCodigo(String codigo) throws Exception;
}