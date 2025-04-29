package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProveedorDAOImpl implements CrudDAO<Proveedor, Integer> {

    @Override
    public Proveedor save(Proveedor proveedor) throws Exception {
        String sql = "INSERT INTO Proveedores (nombre, contacto, telefono, correo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getContacto());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getCorreo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del proveedor falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    proveedor.setIdProveedor(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación del proveedor falló, no se obtuvo el ID.");
                }
            }

            return proveedor;
        }
    }

    @Override
    public Proveedor update(Proveedor proveedor) throws Exception {
        String sql = "UPDATE Proveedores SET nombre = ?, contacto = ?, telefono = ?, correo = ? WHERE id_proveedor = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getContacto());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getCorreo());
            stmt.setInt(5, proveedor.getIdProveedor());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización del proveedor falló, ninguna fila afectada.");
            }

            return proveedor;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Proveedores WHERE id_proveedor = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación del proveedor falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<Proveedor> findById(Integer id) throws Exception {
        String sql = "SELECT * FROM Proveedores WHERE id_proveedor = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Proveedor proveedor = new Proveedor(
                            rs.getInt("id_proveedor"),
                            rs.getString("nombre"),
                            rs.getString("contacto"),
                            rs.getString("telefono"),
                            rs.getString("correo")
                    );
                    return Optional.of(proveedor);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Proveedor> findAll() throws Exception {
        String sql = "SELECT * FROM Proveedores";
        List<Proveedor> proveedores = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                        rs.getInt("id_proveedor"),
                        rs.getString("nombre"),
                        rs.getString("contacto"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                );
                proveedores.add(proveedor);
            }

            return proveedores;
        }
    }
}