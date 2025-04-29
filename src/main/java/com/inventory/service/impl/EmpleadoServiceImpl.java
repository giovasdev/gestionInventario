package com.inventory.service.impl;

import com.inventory.dao.CrudDAO;
import com.inventory.model.Empleado;
import com.inventory.service.EmpleadoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmpleadoServiceImpl implements EmpleadoService {
    private final CrudDAO<Empleado, Integer> empleadoDAO;

    public EmpleadoServiceImpl(CrudDAO<Empleado, Integer> empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    @Override
    public Empleado save(Empleado empleado) throws Exception {
        // Validaciones de negocio
        validarEmpleado(empleado);
        if (existeCorreo(empleado.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un empleado con este correo electrónico.");
        }
        return empleadoDAO.save(empleado);
    }

    @Override
    public Empleado update(Empleado empleado) throws Exception {
        // Validaciones de negocio
        if (empleado.getIdEmpleado() == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo para actualizar.");
        }
        validarEmpleado(empleado);

        // Verificar si el correo ya existe para otro empleado
        Optional<Empleado> existente = empleadoDAO.findById(empleado.getIdEmpleado());
        if (existente.isPresent() && !existente.get().getCorreo().equals(empleado.getCorreo())) {
            if (existeCorreo(empleado.getCorreo())) {
                throw new IllegalArgumentException("Ya existe otro empleado con este correo electrónico.");
            }
        }

        return empleadoDAO.update(empleado);
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo.");
        }
        empleadoDAO.delete(id);
    }

    @Override
    public Optional<Empleado> findById(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo.");
        }
        return empleadoDAO.findById(id);
    }

    @Override
    public List<Empleado> findAll() throws Exception {
        return empleadoDAO.findAll();
    }

    @Override
    public List<Empleado> findByRol(Integer idRol) throws Exception {
        if (idRol == null) {
            throw new IllegalArgumentException("El ID del rol no puede ser nulo.");
        }
        return empleadoDAO.findAll().stream()
                .filter(empleado -> empleado.getRol().getIdRol().equals(idRol))
                .collect(Collectors.toList());
    }

    @Override
    public List<Empleado> findByNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        return empleadoDAO.findAll().stream()
                .filter(empleado -> empleado.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeCorreo(String correo) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        return empleadoDAO.findAll().stream()
                .anyMatch(empleado -> empleado.getCorreo().equalsIgnoreCase(correo));
    }

    private void validarEmpleado(Empleado empleado) {
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado no puede estar vacío.");
        }
        if (empleado.getCorreo() == null || empleado.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del empleado no puede estar vacío.");
        }
        if (!empleado.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
        }
        if (empleado.getTelefono() == null || empleado.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del empleado no puede estar vacío.");
        }
        if (empleado.getRol() == null || empleado.getRol().getIdRol() == null) {
            throw new IllegalArgumentException("El rol del empleado no puede ser nulo.");
        }
    }
}