package com.ngoconnect.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DonationDto {

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    @NotNull(message = "Donation amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Donation amount must be greater than 0")
    private BigDecimal amount;

    private Boolean isAnonymous = false;

    private String message;

    // Constructors
    public DonationDto() {}

    public DonationDto(Long campaignId, BigDecimal amount, Boolean isAnonymous) {
        this.campaignId = campaignId;
        this.amount = amount;
        this.isAnonymous = isAnonymous;
    }

    // Getters and Setters
    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

