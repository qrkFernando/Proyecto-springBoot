# 🤝 Contribuyendo a Librería Online

¡Gracias por tu interés en contribuir a este proyecto! Esta guía te ayudará a empezar.

## 🚀 Cómo Contribuir

### 1. Fork y Clone
```bash
# Fork el repositorio en GitHub
# Luego clona tu fork
git clone https://github.com/tu-usuario/libreria-online.git
cd libreria-online
```

### 2. Configurar el Entorno
```bash
# Instalar dependencias
mvn clean install

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run
```

### 3. Crear una Rama
```bash
git checkout -b feature/nueva-funcionalidad
# o
git checkout -b bugfix/corregir-problema
```

### 4. Realizar Cambios
- Sigue las convenciones de código existentes
- Escribe tests para nuevas funcionalidades
- Actualiza la documentación si es necesario

### 5. Commit y Push
```bash
git add .
git commit -m "feat: agregar nueva funcionalidad"
git push origin feature/nueva-funcionalidad
```

### 6. Pull Request
- Crea un Pull Request desde tu rama
- Describe claramente los cambios realizados
- Referencia issues relacionados

## 📋 Estándares de Código

### Convenciones de Naming
- **Classes**: PascalCase (`BookService`, `CartController`)
- **Methods**: camelCase (`findBookById`, `addToCart`)
- **Variables**: camelCase (`bookList`, `totalPrice`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_CART_ITEMS`)

### Estructura de Commits
```
tipo(ámbito): descripción breve

Descripción más detallada si es necesario
```

**Tipos de commit:**
- `feat`: Nueva funcionalidad
- `fix`: Corrección de bug
- `docs`: Documentación
- `style`: Formato, punto y coma faltante, etc.
- `refactor`: Refactorización de código
- `test`: Agregar tests
- `chore`: Tareas de mantenimiento

### Tests
- Todos los nuevos features deben incluir tests
- Mantén la cobertura de tests alta
- Usa nombres descriptivos para los tests

```java
@Test
void shouldAddBookToCartWhenBookIsAvailable() {
    // Arrange
    Book book = new Book("Test Book", "Test Author", 10.99);
    
    // Act
    cartService.addToCart(book, 1);
    
    // Assert
    assertEquals(1, cartService.getCartSize());
}
```

## 🐛 Reportar Bugs

Al reportar un bug, incluye:
- Descripción clara del problema
- Pasos para reproducir
- Comportamiento esperado vs actual
- Screenshots si es aplicable
- Información del entorno (OS, Java version, etc.)

## 💡 Sugerir Funcionalidades

Para sugerir nuevas funcionalidades:
- Usa GitHub Issues con la etiqueta `enhancement`
- Describe claramente el problema que resuelve
- Proporciona ejemplos de uso
- Considera la compatibilidad con funcionalidades existentes

## 🔄 Proceso de Review

1. **Automated Checks**: CI/CD debe pasar
2. **Code Review**: Al menos un maintainer debe aprobar
3. **Testing**: Tests deben pasar y mantener cobertura
4. **Documentation**: Actualizar docs si es necesario

## 📚 Recursos Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)

## ❓ ¿Necesitas Ayuda?

- Abre un issue con la etiqueta `question`
- Revisa issues existentes
- Consulta la documentación del proyecto

---

¡Gracias por contribuir! 🎉