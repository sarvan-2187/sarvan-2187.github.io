package com.ngoconnect.repository;

import com.ngoconnect.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    
    List<Donation> findByDonorId(Long donorId);
    
    List<Donation> findByCampaignId(Long campaignId);
    
    Optional<Donation> findByTransactionId(String transactionId);
    
    @Query("SELECT d FROM Donation d WHERE d.campaign.ngo.id = :ngoId")
    List<Donation> findDonationsByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign.id = :campaignId")
    BigDecimal getTotalDonationsByCampaign(@Param("campaignId") Long campaignId);
    
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.donor.id = :donorId")
    BigDecimal getTotalDonationsByDonor(@Param("donorId") Long donorId);
    
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign.ngo.id = :ngoId")
    BigDecimal getTotalDonationsByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT d FROM Donation d WHERE d.isAnonymous = false ORDER BY d.createdAt DESC")
    List<Donation> findRecentPublicDonations();
    
    @Query("SELECT COUNT(d) FROM Donation d WHERE d.campaign.id = :campaignId")
    long countDonationsByCampaign(@Param("campaignId") Long campaignId);
    
    @Query("SELECT d FROM Donation d ORDER BY d.amount DESC")
    List<Donation> findTopDonations();
}

