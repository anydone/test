package com.treeleaf.payment.endpoint;

import com.oyster.entities.pb.ErrorsPb;
import com.oyster.entities.pb.payment.ProcessPaymentRequestPb;
import com.oyster.entities.pb.payment.ProcessPaymentResponsePb;
import com.treeleaf.payment.facade.ProcessPaymentFacade;
import com.treeleaf.payment.util.Constants;
import com.oyster.rpc.payment.ProcessPaymentRpcGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class ProcessPaymentRpcImpl extends ProcessPaymentRpcGrpc.ProcessPaymentRpcImplBase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessPaymentRpcImpl.class);

    private final ProcessPaymentFacade facade;

    @Autowired
    public ProcessPaymentRpcImpl(ProcessPaymentFacade facade) {
        this.facade = facade;
    }

    @Override
    public void makePayment(ProcessPaymentRequestPb.ProcessPaymentRequest request, StreamObserver<ProcessPaymentResponsePb.ProcessPaymentResponse> responseObserver) {
        String debugId = UUID.randomUUID().toString();
        if (request.hasDebug()) {
            debugId = request.getDebug().getDebugId();
        }

        try (MDC.MDCCloseable mdc = MDC.putCloseable(Constants.DEBUG_ID_MDC, debugId)) {
            logger.debug("Incoming make payment request: {} ", request);

            ProcessPaymentResponsePb.ProcessPaymentResponse response = facade.makePayment(request);
            responseObserver.onNext(response);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ErrorsPb.ErrorMessage errorMessage = ErrorsPb.ErrorMessage.newBuilder()
                    .addMessages(ex.getMessage())
                    .build();
            responseObserver.onNext(ProcessPaymentResponsePb.ProcessPaymentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(true)
                    .setErrorMessage(errorMessage)
                    .build());
        }
        responseObserver.onCompleted();
    }
}
