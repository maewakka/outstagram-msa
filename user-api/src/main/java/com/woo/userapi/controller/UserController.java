package com.woo.userapi.controller;

import com.woo.userapi.dto.redis.UserInfo;
import com.woo.userapi.dto.req.UserReq;
import com.woo.userapi.entity.User;
import com.woo.userapi.service.AuthService;
import com.woo.userapi.service.EmailVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping()
public class UserController {

    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;

    @GetMapping("/code/send")
    public ResponseEntity<String> sendVerifyingCode(@RequestParam(name = "email") String email) {
        emailVerificationService.sendVerificationEmail(email);

        return ResponseEntity.ok("인증코드가 발송되었습니다.");
    }

    @GetMapping("/code/verify")
    public ResponseEntity<String> verifyingCode(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code) {
        emailVerificationService.verifyingCode(email, code);

        return ResponseEntity.ok("인증이 완료되었습니다.");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserReq req) {
        authService.signUp(req);

        return ResponseEntity.ok("회원 가입이 완료되었습니다.");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(HttpServletRequest request, HttpServletResponse response, @RequestBody UserReq userReq) {
        authService.signIn(request, userReq);

        return ResponseEntity.ok("로그인이 완료되었습니다.");
    }

    @GetMapping
    public UserInfo getUser(HttpServletRequest request) {
        return authService.getUser(request);
    }
}
