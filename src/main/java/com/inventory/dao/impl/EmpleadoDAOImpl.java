package com.inventory.dao.impl;

import com.inventory.config.DatabaseConfig;
import com.inventory.dao.CrudDAO;
import com.inventory.model.Empleado;
import com.inventory.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDAOImpl implements CrudDAO<Empleado, Integer> {

    @Override
    public Empleado save(Empleado empleado) throws Exception {
        String sql = "INSERT INTO Empleados (nombre, correo, telefono, id_rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getCorreo());
            stmt.setString(3, empleado.getTelefono());
            stmt.setInt(4, empleado.getRol().getIdRol());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del empleado falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empleado.setIdEmpleado(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La creación del empleado falló, no se obtuvo el ID.");
                }
            }

            return empleado;
        }
    }

    @Override
    public Empleado update(Empleado empleado) throws Exception {
        String sql = "UPDATE Empleados SET nombre = ?, correo = ?, telefono = ?, id_rol = ? WHERE id_empleado = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getCorreo());
            stmt.setString(3, empleado.getTelefono());
            stmt.setInt(4, empleado.getRol().getIdRol());
            stmt.setInt(5, empleado.getIdEmpleado());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La actualización del empleado falló, ninguna fila afectada.");
            }

            return empleado;
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM Empleados WHERE id_empleado = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La eliminación del empleado falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public Optional<Empleado> findById(Integer id) throws Exception {
        String sql = "SELECT e.*, r.nombre_rol, r.descripcion FROM Empleados e " +
                "JOIN Roles r ON e.id_rol = r.id_rol " +
                "WHERE e.id_empleado = ?";

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

                    Empleado empleado = new Empleado(
                            rs.getInt("id_empleado"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rol
                    );
                    return Optional.of(empleado);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Empleado> findAll() throws Exception {
        String sql = "SELECT e.*, r.nombre_rol, r.descripcion FROM Empleados e " +
                "JOIN Roles r ON e.id_rol = r.id_rol";
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rol rol = new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nombre_rol"),
                        rs.getString("descripcion")
                );

                Empleado empleado = new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rol
                );
                empleados.add(empleado);
            }

            return empleados;
        }
    }
}