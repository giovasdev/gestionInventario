package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    private Integer idProveedor;
    private String nombre;
    private String contacto;
    private String telefono;
    private String correo;
}
