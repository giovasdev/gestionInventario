package com.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioActual {
    private Integer idInventario;
    private Producto producto;
    private Ubicacion ubicacion;
    private Integer stockActual;
    private LocalDateTime fechaActualizacion;
}
