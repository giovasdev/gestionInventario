package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    private Integer idRol;
    private String nombreRol;
    private String descripcion;
}
