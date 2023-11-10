package com.treeleaf.payment.endpoint;

import com.oyster.entities.pb.ErrorsPb;
import com.oyster.entities.pb.payment.PaymentDetailRequestPb;
import com.oyster.entities.pb.payment.PaymentTransactionInfoResponsePb.PaymentDetailResponse;
import com.treeleaf.payment.facade.PaymentInfoFacade;
import com.treeleaf.payment.util.Constants;
import com.oyster.rpc.payment.PaymentInfoRpcGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentInfoRpcImpl extends PaymentInfoRpcGrpc.PaymentInfoRpcImplBase {
    private static final Logger logger = LoggerFactory.getLogger(PaymentInfoRpcImpl.class);

    private final PaymentInfoFacade facade;

    @Autowired
    public PaymentInfoRpcImpl(PaymentInfoFacade facade) {
        this.facade = facade;
    }

    @Override
    public void getPaymentTransaction(PaymentDetailRequestPb.PaymentDetailRequest request, StreamObserver<PaymentDetailResponse> responseObserver) {
        String debugId = UUID.randomUUID().toString();
        if(request.hasDebug()) {
            debugId = request.getDebug().getDebugId();
        }

        try(MDC.MDCCloseable mdc = MDC.putCloseable(Constants.DEBUG_ID_MDC, debugId)){
            logger.debug("Incoming get payment request for {}", request.getPaymentId());

            PaymentDetailResponse response = facade.getPayment(request.getPaymentId());

            responseObserver.onNext(response);
        }catch (Exception e) {
            logger.debug(e.getMessage(),e);
            ErrorsPb.ErrorMessage errorMessage = ErrorsPb.ErrorMessage.newBuilder()
                    .setCode(ErrorsPb.ErrorCode.NOTFOUND)
                    .addMessages(e.getMessage())
                    .build();
            responseObserver.onNext(PaymentDetailResponse.newBuilder()
                    .setSuccess(false)
                    .setError(true)
                    .setErrorMessage(errorMessage)
                    .build());
        }
        responseObserver.onCompleted();
    }

}
