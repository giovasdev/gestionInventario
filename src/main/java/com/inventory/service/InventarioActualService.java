package com.inventory.service;

import com.inventory.model.InventarioActual;
import java.util.List;

public interface InventarioActualService extends CrudService<InventarioActual, Integer> {
    // Métodos específicos para la gestión del inventario actual
    List<InventarioActual> findByProducto(Integer idProducto) throws Exception;
    List<InventarioActual> findByUbicacion(Integer idUbicacion) throws Exception;
    List<InventarioActual> findByStockBajo(Integer stockMinimo) throws Exception;
    InventarioActual findByProductoAndUbicacion(Integer idProducto, Integer idUbicacion) throws Exception;
    void actualizarStock(Integer idProducto, Integer idUbicacion, Integer cantidad, String tipoMovimiento) throws Exception;
}