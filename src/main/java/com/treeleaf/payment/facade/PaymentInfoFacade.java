package com.treeleaf.payment.facade;

import com.oyster.entities.pb.payment.PaymentTransactionInfoResponsePb;
import com.oyster.entities.pb.payment.domain.CurrencyPb;
import com.oyster.entities.pb.payment.domain.PaymentPb;
import com.treeleaf.payment.domain.Payment;
import com.treeleaf.payment.exception.PaymentNotFoundException;
import com.treeleaf.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PaymentInfoFacade {
    private static final Logger logger = LoggerFactory.getLogger(PaymentInfoFacade.class);
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentInfoFacade(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentTransactionInfoResponsePb.PaymentDetailResponse getPayment(String id){
        Payment payment = paymentRepository.getPayment(id).orElseThrow(() -> new PaymentNotFoundException("Payment does not exist") );

        logger.debug("Payment: {} ", payment);

        PaymentPb.Payment paymentPb =PaymentPb.Payment.newBuilder()
                .setBusinessId(payment.getBusinessId())
                .setCurrency(CurrencyPb.Currency.valueOf(payment.getCurrency().name()))
                .setCreatedAt(payment.getCreatedAt().toString())
                .setGrossAmount(payment.getGrossAmount().toString())
                .setFee(payment.getFee().toString())
                .setNetAmount(payment.getNetAmount().toString())
                .setPaymentMethod(payment.getPaymentMethod())
                .setStatus(payment.getStatus())
                .setRequesterRefId(payment.getRequesterRefId())
                .setPaymentTransactionId(payment.getId())
                .build();

        return PaymentTransactionInfoResponsePb.PaymentDetailResponse.newBuilder()
                .setSuccess(true)
                .setError(false)
                .setPayment(paymentPb)
                .build();
    }

}
