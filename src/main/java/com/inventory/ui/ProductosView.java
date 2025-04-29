package com.inventory.ui;

import com.inventory.model.Producto;
import com.inventory.service.ProductoService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductosView extends VBox {
    private final ProductoService productoService;
    private TableView<Producto> tablaProductos;
    private ObservableList<Producto> productos;

    public ProductosView(ProductoService productoService) {
        this.productoService = productoService;
        setPadding(new Insets(10));
        setSpacing(10);

        createToolbar();
        createTable();
        loadProductos();
    }

    private void createToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(5));

        Button btnNuevo = new Button("Nuevo Producto");
        btnNuevo.setOnAction(e -> showNuevoProductoDialog());

        Button btnEditar = new Button("Editar");
        btnEditar.setOnAction(e -> {
            Producto selected = tablaProductos.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditarProductoDialog(selected);
            }
        });

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> {
            Producto selected = tablaProductos.getSelectionModel().getSelectedItem();
            if (selected != null) {
                confirmarEliminar(selected);
            }
        });

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar producto...");
        txtBuscar.setPrefWidth(200);

        toolbar.getChildren().addAll(btnNuevo, btnEditar, btnEliminar, txtBuscar);
        getChildren().add(toolbar);
    }

    private void createTable() {
        tablaProductos = new TableView<>();

        TableColumn<Producto, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Producto, String> colDescripcion = new TableColumn<>("Descripción");
        colDescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());

        TableColumn<Producto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(cellData ->
                cellData.getValue().getCategoriaProperty().get().nombreProperty());

        TableColumn<Producto, String> colProveedor = new TableColumn<>("Proveedor");
        colProveedor.setCellValueFactory(cellData ->
                cellData.getValue().getProveedorProperty().get().nombreProperty());

        TableColumn<Producto, Number> colPrecio = new TableColumn<>("Precio Unitario");
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioUnitarioProperty());

        tablaProductos.getColumns().addAll(
                colCodigo, colNombre, colDescripcion, colCategoria, colProveedor, colPrecio
        );

        getChildren().add(tablaProductos);
    }

    private void loadProductos() {
        try {
            productos = FXCollections.observableArrayList(productoService.findAll());
            tablaProductos.setItems(productos);
        } catch (Exception e) {
            showError("Error al cargar productos", e.getMessage());
        }
    }

    private void showNuevoProductoDialog() {
        // Implementar diálogo para nuevo producto
    }

    private void showEditarProductoDialog(Producto producto) {
        // Implementar diálogo para editar producto
    }

    private void confirmarEliminar(Producto producto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este producto?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productoService.delete(producto.getIdProducto());
                    productos.remove(producto);
                } catch (Exception e) {
                    showError("Error al eliminar", e.getMessage());
                }
            }
        });
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}