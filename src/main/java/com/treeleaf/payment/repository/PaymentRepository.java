package com.treeleaf.payment.repository;

import com.treeleaf.payment.domain.Payment;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class PaymentRepository {

    private ConcurrentMap<String, Payment> payments;

    public PaymentRepository() {
        this.payments = new ConcurrentHashMap<>();
    }

    public void save(Payment payment){
        payments.put(payment.getId(),payment);
    }

    public void update(Payment payment){
        payments.replace(payment.getId(),payment);
    }

    public Optional<Payment> getPayment(String id) {
        return Optional.ofNullable(payments.getOrDefault(id,null));
    }
}
