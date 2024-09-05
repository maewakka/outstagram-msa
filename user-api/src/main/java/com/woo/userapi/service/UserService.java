package com.woo.userapi.service;

import com.google.api.Http;
import com.woo.userapi.dto.redis.UserInfo;
import com.woo.userapi.util.SessionUtil;
import io.grpc.stub.StreamObserver;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SessionUtil sessionUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void getUserInfo(UserServiceProto.UserServiceRequest request, StreamObserver<UserServiceProto.UserServiceResponse> responseObserver) {
        String sessionId = request.getSessionId();
//        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(sessionId);
        UserInfo userInfo = UserInfo.builder()
                .id(1l)
                .nickName("test")
                .email("test@naver.com")
                .phone("010-0000-0000")
                .profileImgUrl("/img/default_img")
                .build();

        UserServiceProto.UserServiceResponse response = UserServiceProto.UserServiceResponse.newBuilder()
                .setId(userInfo.getId())
                .setNickName(userInfo.getNickName())
                .setEmail(userInfo.getEmail())
                .setPhone(userInfo.getPhone())
                .setProfileImageUrl(userInfo.getProfileImgUrl())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void getUserInfo(HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        log.info(userInfo.toString());
    }
}
