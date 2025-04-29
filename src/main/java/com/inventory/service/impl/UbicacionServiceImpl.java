package com.inventory.service.impl;

import com.inventory.dao.CrudDAO;
import com.inventory.model.Ubicacion;
import com.inventory.service.UbicacionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UbicacionServiceImpl implements UbicacionService {
    private final CrudDAO<Ubicacion, Integer> ubicacionDAO;

    public UbicacionServiceImpl(CrudDAO<Ubicacion, Integer> ubicacionDAO) {
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public Ubicacion save(Ubicacion ubicacion) throws Exception {
        // Validaciones de negocio
        validarUbicacion(ubicacion);
        if (existeNombre(ubicacion.getNombre())) {
            throw new IllegalArgumentException("Ya existe una ubicación con este nombre.");
        }
        return ubicacionDAO.save(ubicacion);
    }

    @Override
    public Ubicacion update(Ubicacion ubicacion) throws Exception {
        // Validaciones de negocio
        if (ubicacion.getIdUbicacion() == null) {
            throw new IllegalArgumentException("El ID de la ubicación no puede ser nulo para actualizar.");
        }
        validarUbicacion(ubicacion);

        // Verificar si el nombre ya existe para otra ubicación
        Optional<Ubicacion> existente = ubicacionDAO.findById(ubicacion.getIdUbicacion());
        if (existente.isPresent() && !existente.get().getNombre().equals(ubicacion.getNombre())) {
            if (existeNombre(ubicacion.getNombre())) {
                throw new IllegalArgumentException("Ya existe otra ubicación con este nombre.");
            }
        }

        return ubicacionDAO.update(ubicacion);
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la ubicación no puede ser nulo.");
        }
        ubicacionDAO.delete(id);
    }

    @Override
    public Optional<Ubicacion> findById(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la ubicación no puede ser nulo.");
        }
        return ubicacionDAO.findById(id);
    }

    @Override
    public List<Ubicacion> findAll() throws Exception {
        return ubicacionDAO.findAll();
    }

    @Override
    public List<Ubicacion> findByNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        return ubicacionDAO.findAll().stream()
                .filter(ubicacion -> ubicacion.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ubicacion> findByArea(String area) throws Exception {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("El área no puede estar vacía.");
        }
        return ubicacionDAO.findAll().stream()
                .filter(ubicacion -> ubicacion.getArea().toLowerCase().contains(area.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        return ubicacionDAO.findAll().stream()
                .anyMatch(ubicacion -> ubicacion.getNombre().equalsIgnoreCase(nombre));
    }

    private void validarUbicacion(Ubicacion ubicacion) {
        if (ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación no puede estar vacío.");
        }
        if (ubicacion.getArea() == null || ubicacion.getArea().trim().isEmpty()) {
            throw new IllegalArgumentException("El área de la ubicación no puede estar vacía.");
        }
        if (ubicacion.getDescripcion() == null || ubicacion.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la ubicación no puede estar vacía.");
        }
    }
}