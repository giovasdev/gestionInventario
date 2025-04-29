package com.inventory.ui;

import com.inventory.model.InventarioActual;
import com.inventory.service.InventarioActualService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventarioView extends VBox {
    private final InventarioActualService inventarioService;
    private TableView<InventarioActual> tablaInventario;
    private ObservableList<InventarioActual> inventarios;

    public InventarioView(InventarioActualService inventarioService) {
        this.inventarioService = inventarioService;
        setPadding(new Insets(10));
        setSpacing(10);

        createToolbar();
        createTable();
        createFilters();
        loadInventario();
    }

    private void createToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(5));

        Button btnActualizar = new Button("Actualizar Stock");
        btnActualizar.setOnAction(e -> showActualizarStockDialog());

        Button btnReporte = new Button("Generar Reporte");
        btnReporte.setOnAction(e -> generarReporte());

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar en inventario...");
        txtBuscar.setPrefWidth(200);

        toolbar.getChildren().addAll(btnActualizar, btnReporte, txtBuscar);
        getChildren().add(toolbar);
    }

    private void createFilters() {
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(5));

        ComboBox<String> cmbUbicacion = new ComboBox<>();
        cmbUbicacion.setPromptText("Filtrar por ubicación");

        ComboBox<String> cmbCategoria = new ComboBox<>();
        cmbCategoria.setPromptText("Filtrar por categoría");

        CheckBox chkStockBajo = new CheckBox("Mostrar stock bajo");

        filterBar.getChildren().addAll(cmbUbicacion, cmbCategoria, chkStockBajo);
        getChildren().add(filterBar);
    }

    private void createTable() {
        tablaInventario = new TableView<>();

        TableColumn<InventarioActual, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(cellData ->
                cellData.getValue().getProducto().nombreProperty());

        TableColumn<InventarioActual, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(cellData ->
                cellData.getValue().getProducto().codigoProperty());

        TableColumn<InventarioActual, String> colUbicacion = new TableColumn<>("Ubicación");
        colUbicacion.setCellValueFactory(cellData ->
                cellData.getValue().getUbicacion().nombreProperty());

        TableColumn<InventarioActual, Number> colStock = new TableColumn<>("Stock Actual");
        colStock.setCellValueFactory(cellData ->
                cellData.getValue().cantidadProperty());

        TableColumn<InventarioActual, String> colFecha = new TableColumn<>("Última Actualización");
        colFecha.setCellValueFactory(cellData ->
                cellData.getValue().fechaActualizacionProperty().asString());

        tablaInventario.getColumns().addAll(
                colProducto, colCodigo, colUbicacion, colStock, colFecha
        );

        getChildren().add(tablaInventario);
    }

    private void loadInventario() {
        try {
            inventarios = FXCollections.observableArrayList(inventarioService.findAll());
            tablaInventario.setItems(inventarios);
        } catch (Exception e) {
            showError("Error al cargar inventario", e.getMessage());
        }
    }

    private void showActualizarStockDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar Stock");
        dialog.setHeaderText("Ingrese los datos para actualizar el stock");

        // Crear el formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> cmbProducto = new ComboBox<>();
        ComboBox<String> cmbUbicacion = new ComboBox<>();
        TextField txtCantidad = new TextField();
        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("ENTRADA", "SALIDA");

        grid.add(new Label("Producto:"), 0, 0);
        grid.add(cmbProducto, 1, 0);
        grid.add(new Label("Ubicación:"), 0, 1);
        grid.add(cmbUbicacion, 1, 1);
        grid.add(new Label("Cantidad:"), 0, 2);
        grid.add(txtCantidad, 1, 2);
        grid.add(new Label("Tipo:"), 0, 3);
        grid.add(cmbTipo, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Implementar la actualización del stock
            }
        });
    }

    private void generarReporte() {
        // Implementar la generación de reportes
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}