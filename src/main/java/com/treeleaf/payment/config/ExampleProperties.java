package com.treeleaf.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix="java.service")
public class ExampleProperties {

    @NotNull
    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static class Server{
        @NotNull
        private Grpc grpc;

        public Grpc getGrpc() {
            return grpc;
        }

        public void setGrpc(Grpc grpc) {
            this.grpc = grpc;
        }

        public static class Grpc{
            private int port;

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }
        }
    }
}
