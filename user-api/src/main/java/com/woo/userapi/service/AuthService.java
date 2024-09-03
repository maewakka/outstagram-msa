package com.woo.userapi.service;

import com.woo.exception.util.BizException;
import com.woo.userapi.dto.redis.UserInfo;
import com.woo.userapi.dto.req.UserReq;
import com.woo.userapi.entity.User;
import com.woo.userapi.entity.repository.UserRepository;
import com.woo.userapi.util.MinioUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MinioUtil minioUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(final UserReq userReq) {
        if(userRepository.existsByEmail(userReq.getEmail())) throw new BizException("email_already_exist");

        String encodingPassword = passwordEncoder.encode(userReq.getPassword());
        userRepository.save(userReq.toEntity(encodingPassword));
    }

    @Transactional(readOnly = true)
    public void signIn(HttpServletRequest request, final UserReq userReq) {
        User signInUser = userRepository.findUserByEmail(userReq.getEmail()).orElseThrow(() -> new BizException("login_fail"));

        if(!passwordEncoder.matches(userReq.getPassword(), signInUser.getPassword())) throw new BizException("login_fail");

        UserInfo userInfo = UserInfo.of(signInUser, minioUtil.getUrlFromMinioObject(signInUser.getProfileImgUrl()));
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userInfo);
        session.setMaxInactiveInterval(3600);
    }

    public UserInfo getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

        if(userInfo == null) throw new BizException("expired_authentication");

        return userInfo;
    }
}
