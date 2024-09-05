package com.woo.userapi.service;

import com.woo.exception.util.BizException;
import com.woo.userapi.dto.redis.UserInfo;
import com.woo.userapi.dto.req.UserReq;
import com.woo.userapi.entity.User;
import com.woo.userapi.entity.repository.UserRepository;
import com.woo.userapi.util.MinioUtil;
import com.woo.userapi.util.SessionUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.com.proto.UserServiceProto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MinioUtil minioUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionUtil sessionUtil;

    @Transactional
    public void signUp(final UserReq userReq) {
        if(userRepository.existsByEmail(userReq.getEmail())) throw new BizException("email_already_exist");

        String encodingPassword = passwordEncoder.encode(userReq.getPassword());
        userRepository.save(userReq.toEntity(encodingPassword));
    }

    @Transactional(readOnly = true)
    public void signIn(HttpServletRequest request, HttpServletResponse response, final UserReq userReq) {
        User signInUser = userRepository.findUserByEmail(userReq.getEmail()).orElseThrow(() -> new BizException("login_fail"));
        if(!passwordEncoder.matches(userReq.getPassword(), signInUser.getPassword())) throw new BizException("login_fail");

        UserInfo userInfo = UserInfo.of(signInUser, minioUtil.getUrlFromMinioObject(signInUser.getProfileImgUrl()));

        if(sessionUtil.getSessionIdInCookie(request).isEmpty()) {
            UUID uuid = UUID.randomUUID();
            redisTemplate.opsForValue().set(uuid.toString(), userInfo,  1, TimeUnit.HOURS);

            Cookie cookie = new Cookie("SESSION_ID", uuid.toString());
            cookie.setPath("/");
            cookie.setMaxAge(3600);

            response.addCookie(cookie);
        } else {
            redisTemplate.opsForValue().set(sessionUtil.getSessionIdInCookie(request), userInfo, 1, TimeUnit.HOURS);
        }
    }

    public UserInfo getUser(HttpServletRequest request) {
//        String sessionId = sessionUtil.getSessionIdInCookie(request);
//        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(sessionId);

        UserInfo userInfo = UserInfo.builder()
                .id(1l)
                .nickName("test")
                .email("test@naver.com")
                .phone("010-0000-0000")
                .profileImgUrl("/img/default_img")
                .build();

        if(userInfo == null) throw new BizException("expired_authentication");

        return userInfo;
    }
}
