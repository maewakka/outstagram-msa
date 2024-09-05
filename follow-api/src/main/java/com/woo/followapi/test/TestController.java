package com.woo.followapi.test;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import woo.com.proto.UserServiceGrpc;
import woo.com.proto.UserServiceProto;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GetMapping("/test")
    public void test() {
        gRPCtest(500);
        HttpTest(500);
    }

    private void HttpTest(Integer count) {
        RestTemplate restTemplate = new RestTemplate();
        String targetUrl = "http://maewakka123.iptime.org:31778/users";  // 예시 URL
        Instant start = Instant.now();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "SESSION_ID=06eaa162-6be8-4d6c-9bc1-c47b619c7a01");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for(int i=0; i<count; i++) {
            ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.info("HTTP 응답 시간: {} ms", duration.toMillis());
    }

    private void gRPCtest(Integer count) {
        String sessionId = "06eaa162-6be8-4d6c-9bc1-c47b619c7a01";
        Instant start = Instant.now();

        for(int i=0; i<count; i++) {
            try {
                UserServiceProto.UserServiceRequest req = UserServiceProto.UserServiceRequest.newBuilder().setSessionId(sessionId).build();
                UserServiceProto.UserServiceResponse response = userServiceBlockingStub.getUserInfo(req);

                // 응답에서 받은 데이터 처리
                UserInfo userInfo = UserInfo.builder()
                        .email(response.getEmail())
                        .id(response.getId())
                        .phone(response.getPhone())
                        .email(response.getEmail())
                        .nickName(response.getNickName())
                        .profileImgUrl(response.getProfileImageUrl())
                        .build();

            } catch (Exception e) {
                log.error("gRPC 요청 실패: {}", e.getMessage(), e);
            }
        }


        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.info("gRPC 응답 시간: {} ms", duration.toMillis());
    }

    private String getSessionIdInCookie(HttpServletRequest request) {
        String sessionId = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_ID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        return sessionId;
    }
}
