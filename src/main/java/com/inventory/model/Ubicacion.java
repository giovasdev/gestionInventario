package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {
    private Integer idUbicacion;
    private String nombre;
    private String direccion;
    private String tipo;
}
