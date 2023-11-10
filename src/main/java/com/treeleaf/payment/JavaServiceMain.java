package com.treeleaf.payment;

import com.treeleaf.payment.config.ExampleProperties;
import com.treeleaf.payment.endpoint.PaymentInfoRpcImpl;
import com.treeleaf.payment.endpoint.ProcessPaymentRpcImpl;
import com.oyster.service.grpc.GrpcMain;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JavaServiceMain extends GrpcMain {

    private static final Logger logger = LoggerFactory.getLogger(JavaServiceMain.class);
    private ServerBuilder<?> serverBuilder;

    @Autowired
    public JavaServiceMain(ExampleProperties exampleProperties,
                           PaymentInfoRpcImpl paymentInfoRpc,
                           ProcessPaymentRpcImpl processPaymentRpc) {
        logger.debug("building gpc server on port: {}", exampleProperties.getServer().getGrpc().getPort());
        serverBuilder = ServerBuilder.forPort(exampleProperties.getServer().getGrpc().getPort())
                                    .addService(paymentInfoRpc)
                                    .addService(processPaymentRpc);
    }

    @Override
    protected void cleanUp() {
        this.stop();
    }

    @Override
    public void configure() {
        this.buildServer("javaServiceBoilerplate", serverBuilder);
    }
}
