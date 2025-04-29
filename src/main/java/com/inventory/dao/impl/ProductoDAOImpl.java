package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.Producto;
import com.inventory.model.Categoria;
import com.inventory.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAOImpl implements CrudDAO<Producto, Integer> {

    @Override
    public Producto save(Producto producto) throws Exception {
        String sql = "INSERT INTO Productos (nombre, codigo, descripcion, id_categoria, id_proveedor, precio_unitario) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCodigo());
            stmt.setString(3, producto.getDescripcion());
            stmt.setInt(4, producto.getCategoria().getIdCategoria());
            stmt.setInt(5, producto.getProveedor().getIdProveedor());
            stmt.setBigDecimal(6, producto.getPrecioUnitario());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del producto falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setIdProducto(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación del producto falló, no se obtuvo el ID.");
                }
            }

            return producto;
        }
    }

    @Override
    public Producto update(Producto producto) throws Exception {
        String sql = "UPDATE Productos SET nombre = ?, codigo = ?, descripcion = ?, " +
                "id_categoria = ?, id_proveedor = ?, precio_unitario = ? " +
                "WHERE id_producto = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCodigo());
            stmt.setString(3, producto.getDescripcion());
            stmt.setInt(4, producto.getCategoria().getIdCategoria());
            stmt.setInt(5, producto.getProveedor().getIdProveedor());
            stmt.setBigDecimal(6, producto.getPrecioUnitario());
            stmt.setInt(7, producto.getIdProducto());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización del producto falló, ninguna fila afectada.");
            }

            return producto;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Productos WHERE id_producto = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación del producto falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<Producto> findById(Integer id) throws Exception {
        String sql = "SELECT p.*, c.nombre as categoria_nombre, c.descripcion as categoria_descripcion, " +
                "pr.nombre as proveedor_nombre, pr.contacto, pr.telefono, pr.correo " +
                "FROM Productos p " +
                "JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                "JOIN Proveedores pr ON p.id_proveedor = pr.id_proveedor " +
                "WHERE p.id_producto = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
                            rs.getString("nombre"),
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            categoria,
                            proveedor,
                            rs.getBigDecimal("precio_unitario")
                    );
                    return Optional.of(producto);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Producto> findAll() throws Exception {
        String sql = "SELECT p.*, c.nombre as categoria_nombre, c.descripcion as categoria_descripcion, " +
                "pr.nombre as proveedor_nombre, pr.contacto, pr.telefono, pr.correo " +
                "FROM Productos p " +
                "JOIN Categorias c ON p.id_categoria = c.id_categoria " +
                "JOIN Proveedores pr ON p.id_proveedor = pr.id_proveedor";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
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
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        categoria,
                        proveedor,
                        rs.getBigDecimal("precio_unitario")
                );
                productos.add(producto);
            }

            return productos;
        }
    }
}