package com.inventory.service.impl;

import com.inventory.dao.CrudDAO;
import com.inventory.model.MovimientoInventario;
import com.inventory.service.MovimientoInventarioService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {
    private final CrudDAO<MovimientoInventario, Integer> movimientoDAO;

    public MovimientoInventarioServiceImpl(CrudDAO<MovimientoInventario, Integer> movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    @Override
    public MovimientoInventario save(MovimientoInventario movimiento) throws Exception {
        // Validaciones de negocio
        validarMovimiento(movimiento);
        return movimientoDAO.save(movimiento);
    }

    @Override
    public MovimientoInventario update(MovimientoInventario movimiento) throws Exception {
        // Validaciones de negocio
        if (movimiento.getIdMovimiento() == null) {
            throw new IllegalArgumentException("El ID del movimiento no puede ser nulo para actualizar.");
        }
        validarMovimiento(movimiento);
        return movimientoDAO.update(movimiento);
    }

    @Override
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del movimiento no puede ser nulo.");
        }
        movimientoDAO.delete(id);
    }

    @Override
    public Optional<MovimientoInventario> findById(Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del movimiento no puede ser nulo.");
        }
        return movimientoDAO.findById(id);
    }

    @Override
    public List<MovimientoInventario> findAll() throws Exception {
        return movimientoDAO.findAll();
    }

    @Override
    public List<MovimientoInventario> findByProducto(Integer idProducto) throws Exception {
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo.");
        }
        return movimientoDAO.findAll().stream()
                .filter(movimiento -> movimiento.getProducto().getIdProducto().equals(idProducto))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventario> findByEmpleado(Integer idEmpleado) throws Exception {
        if (idEmpleado == null) {
            throw new IllegalArgumentException("El ID del empleado no puede ser nulo.");
        }
        return movimientoDAO.findAll().stream()
                .filter(movimiento -> movimiento.getEmpleado().getIdEmpleado().equals(idEmpleado))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventario> findByTipoMovimiento(String tipoMovimiento) throws Exception {
        if (tipoMovimiento == null || tipoMovimiento.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento no puede estar vacío.");
        }
        return movimientoDAO.findAll().stream()
                .filter(movimiento -> movimiento.getTipoMovimiento().equalsIgnoreCase(tipoMovimiento))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventario> findByFecha(Date fechaInicio, Date fechaFin) throws Exception {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin.");
        }
        return movimientoDAO.findAll().stream()
                .filter(movimiento -> !movimiento.getFecha().before(fechaInicio) &&
                        !movimiento.getFecha().after(fechaFin))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventario> findByUbicacion(Integer idUbicacion) throws Exception {
        if (idUbicacion == null) {
            throw new IllegalArgumentException("El ID de la ubicación no puede ser nulo.");
        }
        return movimientoDAO.findAll().stream()
                .filter(movimiento -> movimiento.getUbicacion().getIdUbicacion().equals(idUbicacion))
                .collect(Collectors.toList());
    }

    private void validarMovimiento(MovimientoInventario movimiento) {
        if (movimiento.getProducto() == null || movimiento.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("El producto del movimiento no puede ser nulo.");
        }
        if (movimiento.getEmpleado() == null || movimiento.getEmpleado().getIdEmpleado() == null) {
            throw new IllegalArgumentException("El empleado del movimiento no puede ser nulo.");
        }
        if (movimiento.getUbicacion() == null || movimiento.getUbicacion().getIdUbicacion() == null) {
            throw new IllegalArgumentException("La ubicación del movimiento no puede ser nula.");
        }
        if (movimiento.getTipoMovimiento() == null || movimiento.getTipoMovimiento().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento no puede estar vacío.");
        }
        if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (movimiento.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del movimiento no puede ser nula.");
        }
    }
}