package com.ngoconnect.repository;

import com.ngoconnect.model.ImpactReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ImpactReportRepository extends JpaRepository<ImpactReport, Long> {
    
    List<ImpactReport> findByCampaignId(Long campaignId);
    
    @Query("SELECT ir FROM ImpactReport ir WHERE ir.campaign.ngo.id = :ngoId")
    List<ImpactReport> findReportsByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT ir FROM ImpactReport ir WHERE ir.campaign.ngo.isApproved = true ORDER BY ir.createdAt DESC")
    List<ImpactReport> findRecentReportsFromApprovedNGOs();
    
    @Query("SELECT SUM(ir.amountUsed) FROM ImpactReport ir WHERE ir.campaign.id = :campaignId")
    BigDecimal getTotalAmountUsedByCampaign(@Param("campaignId") Long campaignId);
    
    @Query("SELECT SUM(ir.beneficiariesCount) FROM ImpactReport ir WHERE ir.campaign.ngo.id = :ngoId")
    Long getTotalBeneficiariesByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT COUNT(ir) FROM ImpactReport ir WHERE ir.campaign.id = :campaignId")
    long countReportsByCampaign(@Param("campaignId") Long campaignId);
}

