package com.treeleaf.payment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private String id;

    private String businessId;

    private Currency currency;

    private String paymentMethod;

    private String requesterRefId;

    private String status;

    private String pspTransactionId;

    private BigDecimal grossAmount;

    private BigDecimal fee;

    private BigDecimal netAmount;

    private BigDecimal settledAmount;

    private LocalDateTime settledAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRequesterRefId() {
        return requesterRefId;
    }

    public void setRequesterRefId(String requesterRefId) {
        this.requesterRefId = requesterRefId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPspTransactionId() {
        return pspTransactionId;
    }

    public void setPspTransactionId(String pspTransactionId) {
        this.pspTransactionId = pspTransactionId;
    }

    public BigDecimal getGrossAmount() { return grossAmount; }

    public void setGrossAmount(BigDecimal grossAmount) { this.grossAmount = grossAmount; }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(BigDecimal settledAmount) {
        this.settledAmount = settledAmount;
    }

    public LocalDateTime getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(LocalDateTime settledAt) {
        this.settledAt = settledAt;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", businessId='" + businessId + '\'' +
                ", currency=" + currency +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", requesterRefId='" + requesterRefId + '\'' +
                ", status='" + status + '\'' +
                ", pspTransactionId='" + pspTransactionId + '\'' +
                ", grossAmount=" + grossAmount +
                ", fee=" + fee +
                ", netAmount=" + netAmount +
                ", settledAmount=" + settledAmount +
                ", settledAt=" + settledAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
