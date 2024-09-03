package com.woo.followapi.test;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import woo.com.proto.UserServiceGrpc;
import woo.com.proto.UserServiceProto;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GetMapping
    public void test(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 쿠키 이름이 "SESSION"인 쿠키를 찾음
                if ("SESSION".equals(cookie.getName())) {
//                    UserServiceGrpc.UserServiceBlockingStub stup = UserServiceGrpc.UserServiceBlockingStub.
                    UserServiceProto.UserServiceRequest req = UserServiceProto.UserServiceRequest.newBuilder().setSessionId(cookie.getValue()).build();

                    userServiceBlockingStub.getUserInfo(req);
                }
            }
        }
    }

}
