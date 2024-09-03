package com.woo.followapi.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import woo.com.proto.UserServiceGrpc;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel managedChannel() {
        // gRPC 서버 주소와 포트를 설정합니다.
        return ManagedChannelBuilder.forAddress("localhost", 9999)
                .usePlaintext() // 보안이 필요한 경우 TLS 설정 가능
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(ManagedChannel channel) {
        return UserServiceGrpc.newBlockingStub(channel);
    }
}