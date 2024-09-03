package com.woo.userapi.service;

import com.google.api.Http;
import com.woo.userapi.dto.redis.UserInfo;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import woo.com.proto.UserServiceGrpc;
import woo.com.proto.UserServiceProto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.session.redis.namespace}")
    private String preKey;

    @Override
    public void getUserInfo(UserServiceProto.UserServiceRequest request, StreamObserver<UserServiceProto.UserServiceResponse> response) {
        String sessionId = request.getSessionId();

        String key = preKey + "sessions:" + sessionId;

        log.info(key.toString());
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(key);

        log.info(userInfo.toString());
    }
}
