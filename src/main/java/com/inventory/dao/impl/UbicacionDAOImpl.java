package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.Ubicacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UbicacionDAOImpl implements CrudDAO<Ubicacion, Integer> {

    @Override
    public Ubicacion save(Ubicacion ubicacion) throws Exception {
        String sql = "INSERT INTO Ubicaciones (nombre, direccion, tipo) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ubicacion.getNombre());
            stmt.setString(2, ubicacion.getDireccion());
            stmt.setString(3, ubicacion.getTipo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación de la ubicación falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ubicacion.setIdUbicacion(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación de la ubicación falló, no se obtuvo el ID.");
                }
            }

            return ubicacion;
        }
    }

    @Override
    public Ubicacion update(Ubicacion ubicacion) throws Exception {
        String sql = "UPDATE Ubicaciones SET nombre = ?, direccion = ?, tipo = ? WHERE id_ubicacion = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ubicacion.getNombre());
            stmt.setString(2, ubicacion.getDireccion());
            stmt.setString(3, ubicacion.getTipo());
            stmt.setInt(4, ubicacion.getIdUbicacion());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización de la ubicación falló, ninguna fila afectada.");
            }

            return ubicacion;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Ubicaciones WHERE id_ubicacion = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación de la ubicación falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<Ubicacion> findById(Integer id) throws Exception {
        String sql = "SELECT * FROM Ubicaciones WHERE id_ubicacion = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ubicacion ubicacion = new Ubicacion(
                            rs.getInt("id_ubicacion"),
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("tipo")
                    );
                    return Optional.of(ubicacion);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Ubicacion> findAll() throws Exception {
        String sql = "SELECT * FROM Ubicaciones";
        List<Ubicacion> ubicaciones = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ubicacion ubicacion = new Ubicacion(
                        rs.getInt("id_ubicacion"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("tipo")
                );
                ubicaciones.add(ubicacion);
            }

            return ubicaciones;
        }
    }
}