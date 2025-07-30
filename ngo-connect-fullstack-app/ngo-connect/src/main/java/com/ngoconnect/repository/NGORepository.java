package com.ngoconnect.repository;

import com.ngoconnect.model.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NGORepository extends JpaRepository<NGO, Long> {
    
    Optional<NGO> findByUserId(Long userId);
    
    List<NGO> findByIsApproved(Boolean isApproved);
    
    List<NGO> findByCategory(String category);
    
    @Query("SELECT n FROM NGO n WHERE n.isApproved = true")
    List<NGO> findApprovedNGOs();
    
    @Query("SELECT n FROM NGO n WHERE n.isApproved = false")
    List<NGO> findPendingApprovalNGOs();
    
    @Query("SELECT n FROM NGO n WHERE n.isApproved = true AND " +
           "(n.name LIKE %:search% OR n.description LIKE %:search% OR n.category LIKE %:search%)")
    List<NGO> searchApprovedNGOs(@Param("search") String search);
    
    @Query("SELECT n FROM NGO n WHERE n.isApproved = true AND n.category = :category")
    List<NGO> findApprovedNGOsByCategory(@Param("category") String category);
    
    @Query("SELECT DISTINCT n.category FROM NGO n WHERE n.isApproved = true AND n.category IS NOT NULL")
    List<String> findDistinctCategories();
    
    @Query("SELECT COUNT(n) FROM NGO n WHERE n.isApproved = true")
    long countApprovedNGOs();
}

