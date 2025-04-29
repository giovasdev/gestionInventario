package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    private Integer idEmpleado;
    private String nombre;
    private String correo;
    private String telefono;
    private Rol rol;
}
