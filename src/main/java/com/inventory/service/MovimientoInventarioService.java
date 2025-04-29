package com.inventory.service;

import com.inventory.model.MovimientoInventario;
import java.util.Date;
import java.util.List;

public interface MovimientoInventarioService extends CrudService<MovimientoInventario, Integer>{
    // Métodos específicos para la gestión de movimientos de inventario
    List<MovimientoInventario> findByProducto(Integer idProducto) throws Exception;
    List<MovimientoInventario> findByEmpleado(Integer idEmpleado) throws Exception;
    List<MovimientoInventario> findByTipoMovimiento(String tipoMovimiento) throws Exception;
    List<MovimientoInventario> findByFecha(Date fechaInicio, Date fechaFin) throws Exception;
    List<MovimientoInventario> findByUbicacion(Integer idUbicacion) throws Exception;
}
