package com.example.demo.repository;

import com.example.demo.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    
    // Buscar items por ID de compra
    List<PurchaseItem> findByPurchaseId(Long purchaseId);
    
    // Buscar items por ID de libro
    List<PurchaseItem> findByBookId(Long bookId);
    
    // Obtener estadísticas de libros más vendidos
    @Query("SELECT pi.book.id, pi.bookTitle, pi.bookAuthor, SUM(pi.quantity) as totalSold " +
           "FROM PurchaseItem pi " +
           "GROUP BY pi.book.id, pi.bookTitle, pi.bookAuthor " +
           "ORDER BY totalSold DESC")
    List<Object[]> findBestSellingBooks();
    
    // Obtener la cantidad total vendida de un libro específico
    @Query("SELECT COALESCE(SUM(pi.quantity), 0) FROM PurchaseItem pi WHERE pi.book.id = :bookId")
    Long getTotalQuantitySoldForBook(@Param("bookId") Long bookId);
}