package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private Integer idProducto;
    private String nombre;
    private String codigo;
    private String descripcion;
    private Categoria categoria;
    private Proveedor proveedor;
    private BigDecimal precioUnitario;
}
