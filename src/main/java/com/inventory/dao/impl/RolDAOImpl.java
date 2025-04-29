package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RolDAOImpl implements CrudDAO<Rol, Integer> {

    @Override
    public Rol save(Rol rol) throws Exception {
        String sql = "INSERT INTO Roles (nombre_rol, descripcion) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, rol.getNombreRol());
            stmt.setString(2, rol.getDescripcion());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del rol falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rol.setIdRol(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación del rol falló, no se obtuvo el ID.");
                }
            }

            return rol;
        }
    }

    @Override
    public Rol update(Rol rol) throws Exception {
        String sql = "UPDATE Roles SET nombre_rol = ?, descripcion = ? WHERE id_rol = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rol.getNombreRol());
            stmt.setString(2, rol.getDescripcion());
            stmt.setInt(3, rol.getIdRol());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización del rol falló, ninguna fila afectada.");
            }

            return rol;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Roles WHERE id_rol = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación del rol falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<Rol> findById(Integer id) throws Exception {
        String sql = "SELECT * FROM Roles WHERE id_rol = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol(
                            rs.getInt("id_rol"),
                            rs.getString("nombre_rol"),
                            rs.getString("descripcion")
                    );
                    return Optional.of(rol);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Rol> findAll() throws Exception {
        String sql = "SELECT * FROM Roles";
        List<Rol> roles = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rol rol = new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nombre_rol"),
                        rs.getString("descripcion")
                );
                roles.add(rol);
            }

            return roles;
        }
    }
}