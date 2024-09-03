package com.woo.userapi.config;

import com.woo.userapi.service.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GrpcConfig {

    private final UserService userService;

    @PostConstruct
    public void start() throws IOException {
        new Thread(() -> {
            try {
                Server server = ServerBuilder.forPort(9999)
                        .addService(userService)
                        .build();

                log.info("Starting GRPC Server...");
                server.start();
                log.info("GRPC Server Started!!");
                server.awaitTermination();
            } catch (IOException | InterruptedException e) {
                log.error("Error starting gRPC server", e);
            }
        }).start();
    }

}
