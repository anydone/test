package com.treeleaf.payment.domain;

import java.math.BigDecimal;

public class ProcessPaymentResponse implements PaymentResponse{

    public ProcessPaymentResponse(String requesterRefId,BigDecimal grossAmount,
                                  String paymentTransactionStatus, String businessId,
                                  Currency currency, String paymentId) {
        this.requesterRefId = requesterRefId;
        this.grossAmount = grossAmount;
        this.status= paymentTransactionStatus;
        this.businessId= businessId;
        this.currency = currency;
        this.paymentId = paymentId;
    }

    private String requesterRefId;
    private BigDecimal grossAmount;
    private String status;
    private String businessId;
    private Currency currency;
    private String paymentId;


    public String getBusinessId() { return businessId; }

    public void setBusinessId(String businessId) { this.businessId = businessId; }

    public String getRequesterRefId() {
        return requesterRefId;
    }

    public void setRequesterRefId(String requesterRefId) {
        this.requesterRefId = requesterRefId;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Currency getCurrency() { return currency; }

    public void setCurrency(Currency currency) { this.currency = currency; }

    public String getPaymentId() { return paymentId; }

    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

}
