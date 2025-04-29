package com.inventory.service.impl;

import com.inventory.dao.CrudDAO;
import com.inventory.model.Producto;
import com.inventory.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductoServiceImpl implements ProductoService {
    private final CrudDAO<Producto, Integer> productoDAO;

    public ProductoServiceImpl(CrudDAO<Producto, Integer> productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public Producto save(Producto producto) throws Exception {
        // Validaciones de negocio
        validarProducto(producto);
        if (existeCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con este código.");
        }
        return productoDAO.save(producto);
    }

    @Override
    public Producto update(Producto producto) throws Exception {
        // Validaciones de negocio
        if (producto.getIdProducto() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo para actualizar.");
        }
        validarProducto(producto);

        // Verificar si el código ya existe para otro producto
        Optional<Producto> existente = productoDAO.findById(producto.getIdProducto());
        if (existente.isPresent() && !existente.get().getCodigo().equals(producto.getCodigo())) {
            if (existeCodigo(producto.getCodigo())) {
                throw new IllegalArgumentException("Ya existe otro producto con este código.");
            }
        }

        return productoDAO.update(producto);
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        productoDAO.delete(id);
    }

    @Override
    public Optional<Producto> findById(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        return productoDAO.findById(id);
    }

    @Override
    public List<Producto> findAll() throws Exception {
        return productoDAO.findAll();
    }

    @Override
    public List<Producto> findByCategoria(Integer idCategoria) throws Exception {
        if (idCategoria == null) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo.");
        }
        return productoDAO.findAll().stream()
                .filter(producto -> producto.getCategoria().getIdCategoria().equals(idCategoria))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByProveedor(Integer idProveedor) throws Exception {
        if (idProveedor == null) {
            throw new IllegalArgumentException("El ID del proveedor no puede ser nulo.");
        }
        return productoDAO.findAll().stream()
                .filter(producto -> producto.getProveedor().getIdProveedor().equals(idProveedor))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        return productoDAO.findAll().stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Producto findByCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        return productoDAO.findAll().stream()
                .filter(producto -> producto.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún producto con el código especificado."));
    }

    @Override
    public boolean existeCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        return productoDAO.findAll().stream()
                .anyMatch(producto -> producto.getCodigo().equalsIgnoreCase(codigo));
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto no puede estar vacía.");
        }
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("La categoría del producto no puede ser nula.");
        }
        if (producto.getProveedor() == null || producto.getProveedor().getIdProveedor() == null) {
            throw new IllegalArgumentException("El proveedor del producto no puede ser nulo.");
        }
        if (producto.getPrecioUnitario() == null || producto.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor que cero.");
        }
    }
}