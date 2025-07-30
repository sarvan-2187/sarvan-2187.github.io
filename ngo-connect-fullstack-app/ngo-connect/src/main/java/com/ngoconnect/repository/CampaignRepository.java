package com.ngoconnect.repository;

import com.ngoconnect.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    
    List<Campaign> findByNgoId(Long ngoId);
    
    List<Campaign> findByStatus(Campaign.CampaignStatus status);
    
    @Query("SELECT c FROM Campaign c WHERE c.ngo.isApproved = true AND c.status = 'ACTIVE'")
    List<Campaign> findActiveCampaignsFromApprovedNGOs();
    
    @Query("SELECT c FROM Campaign c WHERE c.ngo.isApproved = true AND " +
           "(c.title LIKE %:search% OR c.description LIKE %:search%)")
    List<Campaign> searchCampaigns(@Param("search") String search);
    
    @Query("SELECT c FROM Campaign c WHERE c.ngo.isApproved = true AND c.ngo.category = :category")
    List<Campaign> findCampaignsByCategory(@Param("category") String category);
    
    @Query("SELECT c FROM Campaign c WHERE c.ngo.id = :ngoId AND c.status = 'ACTIVE'")
    List<Campaign> findActiveCampaignsByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT SUM(c.currentAmount) FROM Campaign c WHERE c.ngo.id = :ngoId")
    BigDecimal getTotalRaisedByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT c FROM Campaign c ORDER BY c.currentAmount DESC")
    List<Campaign> findTopFundedCampaigns();
    
    @Query("SELECT c FROM Campaign c WHERE c.status = 'ACTIVE' ORDER BY c.createdAt DESC")
    List<Campaign> findRecentActiveCampaigns();
}

