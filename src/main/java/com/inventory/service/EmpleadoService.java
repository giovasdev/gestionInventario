package com.inventory.service;

import com.inventory.model.Emppleado;
import java.util.List;

public interface EmpleadoService extends CrudService<Empleado, Integer>{
    // Métodos específicos para la gestión de empleados
    List<Empleado> findByRol(Integer idRol) throws Exception;
    List<Empleado> findByNombre(String nombre) throws Exception;
    boolean existeCorreo(String correo) throws Exception;
}
