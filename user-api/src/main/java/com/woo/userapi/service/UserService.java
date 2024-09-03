package com.woo.userapi.service;

import com.google.api.Http;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woo.com.proto.UserServiceGrpc;
import woo.com.proto.UserServiceProto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUserInfo(UserServiceProto.UserServiceRequest request, StreamObserver<UserServiceProto.UserServiceResponse> response) {
        log.info(request.getSessionId());
    }
}
