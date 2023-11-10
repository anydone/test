package com.treeleaf.payment.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProcessPaymentRequest implements PaymentRequest {

    @NotBlank
    private String businessId;
    @NotBlank
    private String requesterRefId;
    @Positive
    private BigDecimal grossAmount;

    private String description;
    @NotNull
    private Currency currency;

    @NotNull
    private String paymentMethod;

    @NotBlank
    private String payeerName;

    @NotBlank
    private String payeerEmail;
    

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

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPayeerName() { return payeerName; }

    public void setPayeerName(String payeerName) { this.payeerName = payeerName; }

    public String getPayeerEmail() { return payeerEmail; }

    public void setPayeerEmail(String payeerEmail) { this.payeerEmail = payeerEmail; }

}
