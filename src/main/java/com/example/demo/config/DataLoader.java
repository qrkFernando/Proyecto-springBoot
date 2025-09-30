package com.example.demo.config;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base de datos está vacía
        if (bookRepository.count() == 0) {
            loadSampleData();
        }
    }
    
    private void loadSampleData() {
        Book[] books = {
            new Book("El Quijote", "Miguel de Cervantes", 
                "La obra maestra de la literatura española que narra las aventuras de Don Quijote de La Mancha.", 
                new BigDecimal("25.99"), "Clásicos", 10),
                
            new Book("Cien años de soledad", "Gabriel García Márquez", 
                "Una saga familiar que abarca un siglo en el pueblo ficticio de Macondo.", 
                new BigDecimal("22.50"), "Literatura Latinoamericana", 15),
                
            new Book("1984", "George Orwell", 
                "Una distopía que presenta un futuro totalitario donde el pensamiento está controlado.", 
                new BigDecimal("18.99"), "Ciencia Ficción", 20),
                
            new Book("Orgullo y prejuicio", "Jane Austen", 
                "Una novela romántica que explora temas de amor, reputación y clase social en la Inglaterra del siglo XIX.", 
                new BigDecimal("19.99"), "Romance", 12),
                
            new Book("El principito", "Antoine de Saint-Exupéry", 
                "Una fábula poética que cuenta la historia de un pequeño príncipe que viaja por el universo.", 
                new BigDecimal("15.50"), "Infantil", 25),
                
            new Book("Crimen y castigo", "Fiódor Dostoyevski", 
                "Una novela psicológica que explora la mente de un joven que comete un asesinato.", 
                new BigDecimal("28.99"), "Clásicos", 8),
                
            new Book("Harry Potter y la piedra filosofal", "J.K. Rowling", 
                "El primer libro de la saga que presenta al joven mago Harry Potter.", 
                new BigDecimal("24.99"), "Fantasía", 30),
                
            new Book("El código Da Vinci", "Dan Brown", 
                "Un thriller que combina arte, historia y misterio en una trama llena de símbolos.", 
                new BigDecimal("21.99"), "Misterio", 18),
                
            new Book("To Kill a Mockingbird", "Harper Lee", 
                "Una novela sobre la injusticia racial en el sur de Estados Unidos.", 
                new BigDecimal("17.50"), "Drama", 14),
                
            new Book("El hobbit", "J.R.R. Tolkien", 
                "La aventura de Bilbo Bolsón en la Tierra Media.", 
                new BigDecimal("23.99"), "Fantasía", 16),
                
            new Book("Sapiens", "Yuval Noah Harari", 
                "Una breve historia de la humanidad desde la Edad de Piedra hasta el presente.", 
                new BigDecimal("26.99"), "Historia", 22),
                
            new Book("El alquimista", "Paulo Coelho", 
                "La historia de un joven pastor andaluz que viaja por el desierto en busca de un tesoro.", 
                new BigDecimal("16.99"), "Ficción", 20)
        };
        
        // Configurar URLs de imágenes SIN cache-busting para simplificar
        String[] imageUrls = {
            "/images/el-quijote.jpg",           // El Quijote
            "/images/cien-anos-soledad.jpg",    // Cien años de soledad
            "/images/1984.jpg",                 // 1984
            "/images/orgullo-prejuicio.jpg",    // Orgullo y prejuicio
            "/images/el-principito.jpg",        // El principito
            "/images/crimen-castigo.jpg",       // Crimen y castigo
            "/images/harry-potter.jpg",         // Harry Potter
            "/images/codigo-da-vinci.jpg",      // El código Da Vinci
            "/images/to-kill-mockingbird.jpg",  // To Kill a Mockingbird
            "/images/el-hobbit.jpg",            // El hobbit
            "/images/sapiens.jpg",              // Sapiens
            "/images/el-alquimista.jpg"         // El alquimista
        };
        
        for (int i = 0; i < books.length; i++) {
            books[i].setImageUrl(imageUrls[i]);
            bookRepository.save(books[i]);
        }
        
        System.out.println("✅ Datos de prueba cargados: " + books.length + " libros");
    }
}