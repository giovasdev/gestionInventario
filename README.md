# 📦 Sistema de Gestión de Inventario

## 📝 Descripción
Este sistema de gestión de inventario es una aplicación de escritorio desarrollada en Java con JavaFX que permite administrar eficientemente el inventario de productos, movimientos de stock y generación de reportes.

## ✨ Características Principales
- 📊 Gestión de inventario actual con visualización en tiempo real
- 📥 Registro de movimientos de entrada y salida
- 🔍 Filtros avanzados por ubicación, categoría y fechas
- 🔎 Sistema de búsqueda integrado
- 📈 Generación de reportes
- 🎯 Interfaz gráfica intuitiva

## 💻 Requisitos del Sistema
- ☕ Java 11 o superior
- 🎨 JavaFX 11 o superior
- 🗄️ Sistema de gestión de base de datos (por definir)

## 📁 Estructura del Proyecto
```
src/main/java/com/inventory/
├── model/          # Entidades y modelos de datos
├── service/        # Lógica de negocio y servicios
├── ui/             # Interfaces gráficas y vistas
│   ├── InventarioView.java    # Vista principal del inventario
│   └── MovimientosView.java   # Vista de movimientos
└── util/           # Utilidades y helpers
```

## 🚀 Funcionalidades Principales

### 📊 Gestión de Inventario
- 📦 Visualización del stock actual por producto
- 🏷️ Filtrado por ubicación y categoría
- ⚠️ Indicador de stock bajo
- 🔄 Actualización de stock en tiempo real

### 📋 Movimientos de Inventario
- ✏️ Registro de entradas y salidas
- 👤 Seguimiento por empleado
- 📜 Historial de movimientos
- 📅 Filtros por fecha y tipo de movimiento

### 📊 Reportes
- 📈 Generación de reportes de inventario
- 📤 Exportación de movimientos
- 📉 Análisis de stock

## 🖥️ Interfaz de Usuario

### 📋 Vista de Inventario
- 📊 Tabla principal con información de productos
- 🛠️ Barra de herramientas con acciones principales
- 🔍 Filtros dinámicos
- 🔎 Búsqueda en tiempo real

### 📝 Vista de Movimientos
- 📋 Registro detallado de transacciones
- ✏️ Formulario de nuevo movimiento
- 📅 Filtros por período
- 📤 Exportación de datos

## 🛠️ Desarrollo

### 🔧 Tecnologías Utilizadas
- ☕ Java: Lenguaje de programación principal
- 🎨 JavaFX: Framework para la interfaz gráfica
- 📱 FXML: Diseño de interfaces de usuario
- 🎯 CSS: Estilos de la aplicación

### 📐 Patrones de Diseño
- 🏗️ MVC (Modelo-Vista-Controlador)
- 🔧 Servicios para la lógica de negocio
- 👀 Observadores para actualización en tiempo real

## ⚙️ Instalación y Configuración
1. 📥 Clonar el repositorio
2. 🔧 Configurar el entorno de desarrollo con Java y JavaFX
3. 🗄️ Configurar la base de datos (instrucciones pendientes)
4. ▶️ Ejecutar la aplicación

## 📖 Uso
1. 🚀 Iniciar la aplicación
2. 🔄 Navegar entre las vistas de Inventario y Movimientos
3. 🔍 Utilizar los filtros para encontrar información específica
4. ✏️ Realizar operaciones de entrada/salida según necesidad
5. 📊 Generar reportes cuando sea necesario

## 🤝 Contribución
Para contribuir al proyecto:
1. 🔱 Hacer fork del repositorio
2. 🌿 Crear una rama para nuevas características
3. 📤 Enviar pull request con los cambios

## 📈 Estado del Proyecto
🚧 En desarrollo activo - Versión inicial