package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovimientoInventarioDAOImpl implements CrudDAO<MovimientoInventario, Integer> {

    @Override
    public MovimientoInventario save(MovimientoInventario movimiento) throws Exception {
        String sql = "INSERT INTO Movimientos_Inventario (id_producto, id_ubicacion, id_empleado, tipo_movimiento, cantidad, fecha) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, movimiento.getProducto().getIdProducto());
            stmt.setInt(2, movimiento.getUbicacion().getIdUbicacion());
            stmt.setInt(3, movimiento.getEmpleado().getIdEmpleado());
            stmt.setString(4, movimiento.getTipoMovimiento());
            stmt.setInt(5, movimiento.getCantidad());
            stmt.setTimestamp(6, Timestamp.valueOf(movimiento.getFecha()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del movimiento de inventario falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movimiento.setIdMovimiento(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación del movimiento de inventario falló, no se obtuvo el ID.");
                }
            }

            return movimiento;
        }
    }

    @Override
    public MovimientoInventario update(MovimientoInventario movimiento) throws Exception {
        String sql = "UPDATE Movimientos_Inventario SET id_producto = ?, id_ubicacion = ?, id_empleado = ?, " +
                "tipo_movimiento = ?, cantidad = ?, fecha = ? WHERE id_movimiento = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movimiento.getProducto().getIdProducto());
            stmt.setInt(2, movimiento.getUbicacion().getIdUbicacion());
            stmt.setInt(3, movimiento.getEmpleado().getIdEmpleado());
            stmt.setString(4, movimiento.getTipoMovimiento());
            stmt.setInt(5, movimiento.getCantidad());
            stmt.setTimestamp(6, Timestamp.valueOf(movimiento.getFecha()));
            stmt.setInt(7, movimiento.getIdMovimiento());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización del movimiento de inventario falló, ninguna fila afectada.");
            }

            return movimiento;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Movimientos_Inventario WHERE id_movimiento = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación del movimiento de inventario falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<MovimientoInventario> findById(Integer id) throws Exception {
        String sql = "SELECT m.*, p.nombre as producto_nombre, p.codigo, p.descripcion as producto_descripcion, " +
                "p.precio_unitario, u.nombre as ubicacion_nombre, u.direccion, u.tipo, " +
                "e.nombre as empleado_nombre, e.correo, e.telefono, " +
                "c.id_categoria, c.nombre as categoria_nombre, c.descripcion as categoria_descripcion, " +
                "pr.id_proveedor, pr.nombre as proveedor_nombre, pr.contacto, pr.telefono as proveedor_telefono, " +
                "pr.correo as proveedor_correo, r.id_rol, r.nombre_rol, r.descripcion as rol_descripcion " +
                "FROM Movimientos_Inventario m " +
                "JOIN Productos p ON m.id_producto = p.id_producto " +
                "JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                "JOIN Proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                "JOIN Ubicaciones u ON m.id_ubicacion = u.id_ubicacion " +
                "JOIN Empleados e ON m.id_empleado = e.id_empleado " +
                "JOIN Roles r ON e.id_rol = r.id_rol " +
                "WHERE m.id_movimiento = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construirMovimientoInventario(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<MovimientoInventario> findAll() throws Exception {
        String sql = "SELECT m.*, p.nombre as producto_nombre, p.codigo, p.descripcion as producto_descripcion, " +
                "p.precio_unitario, u.nombre as ubicacion_nombre, u.direccion, u.tipo, " +
                "e.nombre as empleado_nombre, e.correo, e.telefono, " +
                "c.id_categoria, c.nombre as categoria_nombre, c.descripcion as categoria_descripcion, " +
                "pr.id_proveedor, pr.nombre as proveedor_nombre, pr.contacto, pr.telefono as proveedor_telefono, " +
                "pr.correo as proveedor_correo, r.id_rol, r.nombre_rol, r.descripcion as rol_descripcion " +
                "FROM Movimientos_Inventario m " +
                "JOIN Productos p ON m.id_producto = p.id_producto " +
                "JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                "JOIN Proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                "JOIN Ubicaciones u ON m.id_ubicacion = u.id_ubicacion " +
                "JOIN Empleados e ON m.id_empleado = e.id_empleado " +
                "JOIN Roles r ON e.id_rol = r.id_rol";
        List<MovimientoInventario> movimientos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movimientos.add(construirMovimientoInventario(rs));
            }

            return movimientos;
        }
    }

    private MovimientoInventario construirMovimientoInventario(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria_nombre"),
                rs.getString("categoria_descripcion")
        );

        Proveedor proveedor = new Proveedor(
                rs.getInt("id_proveedor"),
                rs.getString("proveedor_nombre"),
                rs.getString("contacto"),
                rs.getString("proveedor_telefono"),
                rs.getString("proveedor_correo")
        );

        Producto producto = new Producto(
                rs.getInt("id_producto"),
                rs.getString("producto_nombre"),
                rs.getString("codigo"),
                rs.getString("producto_descripcion"),
                categoria,
                proveedor,
                rs.getBigDecimal("precio_unitario")
        );

        Ubicacion ubicacion = new Ubicacion(
                rs.getInt("id_ubicacion"),
                rs.getString("ubicacion_nombre"),
                rs.getString("direccion"),
                rs.getString("tipo")
        );

        Rol rol = new Rol(
                rs.getInt("id_rol"),
                rs.getString("nombre_rol"),
                rs.getString("rol_descripcion")
        );

        Empleado empleado = new Empleado(
                rs.getInt("id_empleado"),
                rs.getString("empleado_nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rol
        );

        return new MovimientoInventario(
                rs.getInt("id_movimiento"),
                producto,
                ubicacion,
                empleado,
                rs.getString("tipo_movimiento"),
                rs.getInt("cantidad"),
                rs.getTimestamp("fecha").toLocalDateTime()
        );
    }
}