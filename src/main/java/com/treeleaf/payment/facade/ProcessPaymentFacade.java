package com.treeleaf.payment.facade;

import com.oyster.entities.pb.payment.ProcessPaymentRequestPb;
import com.oyster.entities.pb.payment.ProcessPaymentResponsePb;
import com.oyster.entities.pb.payment.domain.CurrencyPb;
import com.treeleaf.payment.domain.Currency;
import com.treeleaf.payment.domain.ProcessPaymentRequest;
import com.treeleaf.payment.service.MakePaymentService;
import com.treeleaf.payment.domain.ProcessPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProcessPaymentFacade {

    private static final Logger logger = LoggerFactory.getLogger(ProcessPaymentFacade.class);

    private final MakePaymentService service;

    @Autowired
    public ProcessPaymentFacade(MakePaymentService service) {
        this.service = service;
    }


    public ProcessPaymentResponsePb.ProcessPaymentResponse makePayment(ProcessPaymentRequestPb.ProcessPaymentRequest request) {
        logger.debug("Processing payment request in Facade");

        logger.debug("This is were we execute all input validations, " +
                "input mapping, and call required business services");

        ProcessPaymentRequest paymentRequest = toProcessPaymentRequest(request);

        ProcessPaymentResponse paymentResponse = service.processPayment(paymentRequest);

        return ProcessPaymentResponsePb.ProcessPaymentResponse.newBuilder()
                .setSuccess(true)
                .setError(false)
                .setBusinessId(paymentResponse.getBusinessId())
                .setRequesterRefId(paymentResponse.getRequesterRefId())
                .setGrossAmount(paymentResponse.getGrossAmount().longValue())
                .setStatus(paymentResponse.getStatus())
                .setCurrency(CurrencyPb.Currency.valueOf(paymentResponse.getCurrency().name()))
                .setPaymentId(paymentResponse.getPaymentId())
                .build();
    }

    private ProcessPaymentRequest toProcessPaymentRequest(ProcessPaymentRequestPb.ProcessPaymentRequest request){

        ProcessPaymentRequest paymentRequest = new ProcessPaymentRequest();
        paymentRequest.setBusinessId(request.getBusinessId());
        paymentRequest.setPaymentMethod(request.getPaymentMethod());
        paymentRequest.setPayeerEmail(request.getPayeerEmail());
        paymentRequest.setPayeerName(request.getPayeerName());
        paymentRequest.setRequesterRefId(request.getRequesterRefId());
        paymentRequest.setCurrency(Currency.valueOfNullable(request.getCurrency()));
        paymentRequest.setGrossAmount(BigDecimal.valueOf(request.getGrossAmount()));
        paymentRequest.setDescription(request.getDescription());

        return paymentRequest;
    }


}
