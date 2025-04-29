package com.inventory.service;

import com.inventory.model.Ubicacion;
import java.util.List;

public interface UbicacionService extends CrudService<Ubicacion, Integer> {
    // Métodos específicos para la gestión de ubicaciones
    List<Ubicacion> findByNombre(String nombre) throws Exception;
    List<Ubicacion> findByArea(String area) throws Exception;
    boolean existeNombre(String nombre) throws Exception;
}