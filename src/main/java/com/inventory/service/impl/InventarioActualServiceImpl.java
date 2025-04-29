package com.inventory.service.impl;

import com.inventory.dao.CrudDAO;
import com.inventory.model.InventarioActual;
import com.inventory.service.InventarioActualService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventarioActualServiceImpl implements InventarioActualService {
    private final CrudDAO<InventarioActual, Integer> inventarioDAO;

    public InventarioActualServiceImpl(CrudDAO<InventarioActual, Integer> inventarioDAO) {
        this.inventarioDAO = inventarioDAO;
    }

    @Override
    public InventarioActual save(InventarioActual inventario) throws Exception {
        // Validaciones de negocio
        validarInventarioActual(inventario);
        return inventarioDAO.save(inventario);
    }

    @Override
    public InventarioActual update(InventarioActual inventario) throws Exception {
        // Validaciones de negocio
        if (inventario.getIdInventarioActual() == null) {
            throw new IllegalArgumentException("El ID del inventario no puede ser nulo para actualizar.");
        }
        validarInventarioActual(inventario);
        return inventarioDAO.update(inventario);
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del inventario no puede ser nulo.");
        }
        inventarioDAO.delete(id);
    }

    @Override
    public Optional<InventarioActual> findById(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del inventario no puede ser nulo.");
        }
        return inventarioDAO.findById(id);
    }

    @Override
    public List<InventarioActual> findAll() throws Exception {
        return inventarioDAO.findAll();
    }

    @Override
    public List<InventarioActual> findByProducto(Integer idProducto) throws Exception {
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        return inventarioDAO.findAll().stream()
                .filter(inventario -> inventario.getProducto().getIdProducto().equals(idProducto))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioActual> findByUbicacion(Integer idUbicacion) throws Exception {
        if (idUbicacion == null) {
            throw new IllegalArgumentException("El ID de la ubicación no puede ser nulo.");
        }
        return inventarioDAO.findAll().stream()
                .filter(inventario -> inventario.getUbicacion().getIdUbicacion().equals(idUbicacion))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioActual> findByStockBajo(Integer stockMinimo) throws Exception {
        if (stockMinimo == null) {
            throw new IllegalArgumentException("El stock mínimo no puede ser nulo.");
        }
        return inventarioDAO.findAll().stream()
                .filter(inventario -> inventario.getCantidad() <= stockMinimo)
                .collect(Collectors.toList());
    }

    @Override
    public InventarioActual findByProductoAndUbicacion(Integer idProducto, Integer idUbicacion) throws Exception {
        if (idProducto == null || idUbicacion == null) {
            throw new IllegalArgumentException("El ID del producto y de la ubicación no pueden ser nulos.");
        }
        return inventarioDAO.findAll().stream()
                .filter(inventario -> inventario.getProducto().getIdProducto().equals(idProducto) &&
                        inventario.getUbicacion().getIdUbicacion().equals(idUbicacion))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el inventario para el producto y ubicación especificados."));
    }

    @Override
    public void actualizarStock(Integer idProducto, Integer idUbicacion, Integer cantidad, String tipoMovimiento) throws Exception {
        if (idProducto == null || idUbicacion == null || cantidad == null || tipoMovimiento == null) {
            throw new IllegalArgumentException("Todos los parámetros son requeridos para actualizar el stock.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        InventarioActual inventario = findByProductoAndUbicacion(idProducto, idUbicacion);
        int nuevoStock;

        switch (tipoMovimiento.toUpperCase()) {
            case "ENTRADA":
                nuevoStock = inventario.getCantidad() + cantidad;
                break;
            case "SALIDA":
                nuevoStock = inventario.getCantidad() - cantidad;
                if (nuevoStock < 0) {
                    throw new IllegalArgumentException("No hay suficiente stock disponible para realizar la salida.");
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido. Debe ser 'ENTRADA' o 'SALIDA'.");
        }

        inventario.setCantidad(nuevoStock);
        update(inventario);
    }

    private void validarInventarioActual(InventarioActual inventario) {
        if (inventario.getProducto() == null || inventario.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("El producto del inventario no puede ser nulo.");
        }
        if (inventario.getUbicacion() == null || inventario.getUbicacion().getIdUbicacion() == null) {
            throw new IllegalArgumentException("La ubicación del inventario no puede ser nula.");
        }
        if (inventario.getCantidad() == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula.");
        }
        if (inventario.getCantidad() < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
    }
}