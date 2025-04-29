package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAOImpl implements CrudDAO<Categoria, Integer> {

    @Override
    public Categoria save(Categoria categoria) throws Exception {
        String sql = "INSERT INTO Categorias (nombre, descripcion) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación de la categoría falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setIdCategoria(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación de la categoría falló, no se obtuvo el ID.");
                }
            }

            return categoria;
        }
    }

    @Override
    public Categoria update(Categoria categoria) throws Exception {
        String sql = "UPDATE Categorias SET nombre = ?, descripcion = ? WHERE id_categoria = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getIdCategoria());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización de la categoría falló, ninguna fila afectada.");
            }

            return categoria;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Categorias WHERE id_categoria = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación de la categoría falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<Categoria> findById(Integer id) throws Exception {
        String sql = "SELECT * FROM Categorias WHERE id_categoria = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria categoria = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                    return Optional.of(categoria);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Categoria> findAll() throws Exception {
        String sql = "SELECT * FROM Categorias";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
                categorias.add(categoria);
            }

            return categorias;
        }
    }
}