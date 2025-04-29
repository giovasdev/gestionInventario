package com.inventory.service.impl;

import com.inventory.dao.CrudDAO;
import com.inventory.model.Categoria;
import com.inventory.service.CategoriaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoriaServiceImpl implements CategoriaService {
    private final CrudDAO<Categoria, Integer> categoriaDAO;

    public CategoriaServiceImpl(CrudDAO<Categoria, Integer> categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public Categoria save(Categoria categoria) throws Exception {
        // Validaciones de negocio
        validarCategoria(categoria);
        if (existeNombre(categoria.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con este nombre.");
        }
        return categoriaDAO.save(categoria);
    }

    @Override
    public Categoria update(Categoria categoria) throws Exception {
        // Validaciones de negocio
        if (categoria.getIdCategoria() == null) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo para actualizar.");
        }
        validarCategoria(categoria);

        // Verificar si el nombre ya existe para otra categoría
        Optional<Categoria> existente = categoriaDAO.findById(categoria.getIdCategoria());
        if (existente.isPresent() && !existente.get().getNombre().equals(categoria.getNombre())) {
            if (existeNombre(categoria.getNombre())) {
                throw new IllegalArgumentException("Ya existe otra categoría con este nombre.");
            }
        }

        return categoriaDAO.update(categoria);
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo.");
        }
        categoriaDAO.delete(id);
    }

    @Override
    public Optional<Categoria> findById(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo.");
        }
        return categoriaDAO.findById(id);
    }

    @Override
    public List<Categoria> findAll() throws Exception {
        return categoriaDAO.findAll();
    }

    @Override
    public List<Categoria> findByNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        return categoriaDAO.findAll().stream()
                .filter(categoria -> categoria.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        return categoriaDAO.findAll().stream()
                .anyMatch(categoria -> categoria.getNombre().equalsIgnoreCase(nombre));
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }
        if (categoria.getDescripcion() == null || categoria.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la categoría no puede estar vacía.");
        }
    }
}