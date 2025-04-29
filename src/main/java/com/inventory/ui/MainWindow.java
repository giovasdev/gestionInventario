package com.inventory.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private BorderPane mainLayout;
    private TabPane tabPane;

    @Override
    public void start(Stage primaryStage) {
        mainLayout = new BorderPane();
        createMenuBar();
        createTabPane();

        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        primaryStage.setTitle("Sistema de Gestión de Inventario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menú Archivo
        Menu fileMenu = new Menu("Archivo");
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        // Menú Ayuda
        Menu helpMenu = new Menu("Ayuda");
        MenuItem aboutItem = new MenuItem("Acerca de");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        mainLayout.setTop(menuBar);
    }

    private void createTabPane() {
        tabPane = new TabPane();

        // Crear pestañas para cada módulo
        Tab productosTab = new Tab("Productos");
        productosTab.setClosable(false);

        Tab inventarioTab = new Tab("Inventario");
        inventarioTab.setClosable(false);

        Tab movimientosTab = new Tab("Movimientos");
        movimientosTab.setClosable(false);

        Tab empleadosTab = new Tab("Empleados");
        empleadosTab.setClosable(false);

        Tab ubicacionesTab = new Tab("Ubicaciones");
        ubicacionesTab.setClosable(false);

        Tab proveedoresTab = new Tab("Proveedores");
        proveedoresTab.setClosable(false);

        // Agregar todas las pestañas
        tabPane.getTabs().addAll(
                productosTab,
                inventarioTab,
                movimientosTab,
                empleadosTab,
                ubicacionesTab,
                proveedoresTab
        );

        mainLayout.setCenter(tabPane);
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Sistema de Gestión de Inventario");
        alert.setContentText("Versión 1.0\nDesarrollado por el equipo de SENA");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}