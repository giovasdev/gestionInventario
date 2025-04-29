package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {
    private Integer idMovimiento;
    private Producto producto;
    private Ubicacion ubicacion;
    private Empleado empleado;
    private String tipoMovimiento; // "entrada" o "salida"
    private Integer cantidad;
    private LocalDateTime fecha;
}
