package com.treeleaf.payment.service;

import com.treeleaf.payment.domain.Payment;
import com.treeleaf.payment.domain.ProcessPaymentRequest;
import com.treeleaf.payment.domain.ProcessPaymentResponse;
import com.treeleaf.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@Service
public class MakePaymentService {
    private static final Logger logger = LoggerFactory.getLogger(MakePaymentService.class);

    private final PaymentRepository paymentRepository;

    @Autowired
    public MakePaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public ProcessPaymentResponse processPayment(@Valid ProcessPaymentRequest paymentRequest){
        logger.debug("Processing payment request for: {} - {}", paymentRequest.getGrossAmount(),
                paymentRequest.getCurrency());

        logger.debug("This is were we execute all business logic, it could include using a repository or calling external API");

        logger.debug("Running business logic... ");

        String paymentId = savePayment(paymentRequest);

        return new ProcessPaymentResponse(
                paymentRequest.getRequesterRefId(),
                paymentRequest.getGrossAmount(),
                "SUCCESS",
                paymentRequest.getBusinessId(),
                paymentRequest.getCurrency(),
                paymentId);
    }

    private String savePayment(ProcessPaymentRequest paymentRequest){
        Payment payment = new Payment();
        payment.setGrossAmount(paymentRequest.getGrossAmount().setScale(4, RoundingMode.HALF_EVEN));
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setBusinessId(paymentRequest.getBusinessId());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setId(UUID.randomUUID().toString());
        payment.setRequesterRefId(paymentRequest.getRequesterRefId());
        payment.setStatus("SUCCESS");
        BigDecimal fee = calculateFee(paymentRequest.getGrossAmount().setScale(4, RoundingMode.HALF_EVEN));
        payment.setFee(fee);
        payment.setNetAmount(paymentRequest.getGrossAmount().subtract(fee).setScale(4, RoundingMode.HALF_EVEN));

        logger.debug("Payment: {} ", payment);

        paymentRepository.save(payment);

        return payment.getId();
    }

    private BigDecimal calculateFee(BigDecimal amount){
        return amount.multiply(new BigDecimal("0.01")).setScale(4, RoundingMode.HALF_EVEN);
    }


}
