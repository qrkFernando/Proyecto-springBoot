# 📚 Librería Online - Spring Boot

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-22-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

Una aplicación web completa para una librería online desarrollada con **Spring Boot**, que permite a los usuarios explorar, buscar y comprar libros con un sistema completo de gestión de carritos y pagos.

## ✨ Características Principales

- 📖 **Catálogo de libros**: Visualización de libros con información detallada
- 🔍 **Búsqueda avanzada**: Búsqueda por título, autor y filtrado por categorías
- 🛒 **Carrito de compras**: Gestión completa del carrito con persistencia de sesión
- 💳 **Sistema de pagos**: Procesamiento de pagos y confirmación de compras
- 📊 **Panel administrativo**: Gestión de ventas y reportes
- 📱 **Diseño responsive**: Compatible con dispositivos móviles y tablets
- 🏷️ **Categorías**: Organización de libros por géneros
- 📈 **Historial de compras**: Seguimiento completo de pedidos

## 🛠️ Stack Tecnológico

### Backend
- **Spring Boot** 3.3.0 - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Web MVC** - Controladores REST y MVC
- **Spring Boot Validation** - Validación de datos
- **H2 Database** - Base de datos en memoria (desarrollo)

### Frontend
- **Thymeleaf** - Motor de plantillas
- **Bootstrap 5** - Framework CSS
- **Font Awesome** - Iconografía
- **JavaScript ES6+** - Funcionalidades interactivas

### Herramientas
- **Maven** - Gestión de dependencias
- **Java 21** - Lenguaje de programación

## 🚀 Inicio Rápido

### Prerrequisitos
- ☕ **Java 17+** (recomendado Java 21)
- 📦 **Maven 3.6+** (opcional, incluye wrapper)
- 🌐 **Navegador web moderno**

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/libreria-online.git
   cd libreria-online
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   # Con Maven instalado
   mvn spring-boot:run
   
   # O usando el wrapper (sin Maven)
   ./mvnw spring-boot:run    # Linux/Mac
   mvnw.cmd spring-boot:run  # Windows
   ```

4. **Acceder a la aplicación**
   - 🌐 **Aplicación**: http://localhost:8082
   - 🗄️ **Base de datos H2**: http://localhost:8082/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Usuario: `sa`
     - Contraseña: `password`

### Perfiles de Configuración

```bash
# Desarrollo (por defecto)
mvn spring-boot:run -Dspring.profiles.active=dev

# Producción
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/          # Controladores REST y MVC
│   │   ├── model/              # Entidades JPA
│   │   ├── repository/         # Repositorios de datos
│   │   ├── service/            # Lógica de negocio
│   │   └── DemoApplication.java # Clase principal
│   └── resources/
│       ├── static/             # Archivos estáticos (CSS, JS)
│       ├── templates/          # Plantillas Thymeleaf
│       ├── application.properties # Configuración
│       └── data.sql           # Datos de prueba
└── test/                      # Tests unitarios
```

## 🎯 Funcionalidades Principales

### 1. Página Principal
- Muestra libros destacados
- Navegación por categorías
- Búsqueda rápida

### 2. Búsqueda y Filtrado
- Búsqueda por título o autor
- Filtrado por categorías
- Resultados paginados

### 3. Detalles del Libro
- Información completa del libro
- Gestión de stock
- Agregar al carrito

### 4. Carrito de Compras
- Agregar/eliminar productos
- Modificar cantidades
- Cálculo de totales
- Persistencia por sesión

## 🗃️ Modelo de Datos

### Book (Libro)
- `id`: Identificador único
- `title`: Título del libro
- `author`: Autor
- `description`: Descripción
- `price`: Precio
- `category`: Categoría
- `stock`: Stock disponible
- `available`: Disponibilidad
- `imageUrl`: URL de la imagen

### CartItem (Item del Carrito)
- `id`: Identificador único
- `book`: Referencia al libro
- `quantity`: Cantidad
- `sessionId`: ID de sesión

## 🎨 Estilos y Diseño

- **Bootstrap 5**: Framework CSS para diseño responsive
- **Font Awesome**: Iconos
- **CSS personalizado**: Estilos adicionales en `/static/css/style.css`
- **JavaScript**: Funcionalidades interactivas en `/static/js/main.js`

## 📊 Datos de Prueba

La aplicación incluye datos de prueba que se cargan automáticamente:
- 12 libros de diferentes categorías
- Categorías: Clásicos, Literatura Latinoamericana, Ciencia Ficción, Romance, Infantil, Fantasía, Misterio, Drama, Historia, Ficción

## 🔍 API Endpoints

### Páginas Web
- `GET /` - Página principal
- `GET /search` - Búsqueda de libros
- `GET /books/{id}` - Detalles del libro
- `GET /books/category/{category}` - Libros por categoría
- `GET /cart` - Carrito de compras

### Acciones del Carrito
- `POST /cart/add` - Agregar al carrito
- `POST /cart/update` - Actualizar cantidad
- `POST /cart/remove` - Eliminar del carrito
- `POST /cart/clear` - Vaciar carrito

## 🚧 Próximas Funcionalidades

- [ ] Sistema de usuarios y autenticación
- [ ] Proceso de checkout y pagos
- [ ] Historial de pedidos
- [ ] Sistema de reseñas y calificaciones
- [ ] Panel de administración
- [ ] API REST completa
- [ ] Integración con base de datos externa

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crea una rama para tu funcionalidad (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Desarrollador

Desarrollado como proyecto de aprendizaje de Spring Boot.

---

¡Disfruta explorando la Librería Online! 📚✨
