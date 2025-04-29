package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventarioActualDAOImpl implements CrudDAO<InventarioActual, Integer> {

    @Override
    public InventarioActual save(InventarioActual inventario) throws Exception {
        String sql = "INSERT INTO Inventario_Actual (id_producto, id_ubicacion, stock_actual, fecha_actualizacion) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, inventario.getProducto().getIdProducto());
            stmt.setInt(2, inventario.getUbicacion().getIdUbicacion());
            stmt.setInt(3, inventario.getStockActual());
            stmt.setTimestamp(4, Timestamp.valueOf(inventario.getFechaActualizacion()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del inventario actual falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inventario.setIdInventario(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación del inventario actual falló, no se obtuvo el ID.");
                }
            }

            return inventario;
        }
    }

    @Override
    public InventarioActual update(InventarioActual inventario) throws Exception {
        String sql = "UPDATE Inventario_Actual SET id_producto = ?, id_ubicacion = ?, " +
                "stock_actual = ?, fecha_actualizacion = ? WHERE id_inventario = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventario.getProducto().getIdProducto());
            stmt.setInt(2, inventario.getUbicacion().getIdUbicacion());
            stmt.setInt(3, inventario.getStockActual());
            stmt.setTimestamp(4, Timestamp.valueOf(inventario.getFechaActualizacion()));
            stmt.setInt(5, inventario.getIdInventario());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización del inventario actual falló, ninguna fila afectada.");
            }

            return inventario;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Inventario_Actual WHERE id_inventario = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación del inventario actual falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<InventarioActual> findById(Integer id) throws Exception {
        String sql = "SELECT i.*, p.nombre as producto_nombre, p.codigo, p.descripcion as producto_descripcion, " +
                "p.precio_unitario, u.nombre as ubicacion_nombre, u.direccion, u.tipo, " +
                "c.id_categoria, c.nombre as categoria_nombre, c.descripcion as categoria_descripcion, " +
                "pr.id_proveedor, pr.nombre as proveedor_nombre, pr.contacto, pr.telefono, pr.correo " +
                "FROM Inventario_Actual i " +
                "JOIN Productos p ON i.id_producto = p.id_producto " +
                "JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                "JOIN Proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                "JOIN Ubicaciones u ON i.id_ubicacion = u.id_ubicacion " +
                "WHERE i.id_inventario = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(construirInventarioActual(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<InventarioActual> findAll() throws Exception {
        String sql = "SELECT i.*, p.nombre as producto_nombre, p.codigo, p.descripcion as producto_descripcion, " +
                "p.precio_unitario, u.nombre as ubicacion_nombre, u.direccion, u.tipo, " +
                "c.id_categoria, c.nombre as categoria_nombre, c.descripcion as categoria_descripcion, " +
                "pr.id_proveedor, pr.nombre as proveedor_nombre, pr.contacto, pr.telefono, pr.correo " +
                "FROM Inventario_Actual i " +
                "JOIN Productos p ON i.id_producto = p.id_producto " +
                "JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                "JOIN Proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                "JOIN Ubicaciones u ON i.id_ubicacion = u.id_ubicacion";
        List<InventarioActual> inventarios = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                inventarios.add(construirInventarioActual(rs));
            }

            return inventarios;
        }
    }

    private InventarioActual construirInventarioActual(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria_nombre"),
                rs.getString("categoria_descripcion")
        );

        Proveedor proveedor = new Proveedor(
                rs.getInt("id_proveedor"),
                rs.getString("proveedor_nombre"),
                rs.getString("contacto"),
                rs.getString("telefono"),
                rs.getString("correo")
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

        return new InventarioActual(
                rs.getInt("id_inventario"),
                producto,
                ubicacion,
                rs.getInt("stock_actual"),
                rs.getTimestamp("fecha_actualizacion").toLocalDateTime()
        );
    }
}