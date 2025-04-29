package com.inventory.service;

public interface RolService extends CrudService<Rol, Integer> {
    // Métodos específicos para la gestión de roles
    List<Rol> findByNombre(String nombre) throws Exception;
    boolean existeNombre(String nombre) throws Exception;
}
