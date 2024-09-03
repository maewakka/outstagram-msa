package com.woo.userapi.service;

import com.woo.exception.util.BizException;
import com.woo.userapi.dto.redis.EmailVerifyingDto;
import com.woo.userapi.entity.repository.UserRepository;
import com.woo.userapi.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailUtil emailUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    public void sendVerificationEmail(String email) {
        String verificationCode = generateRandomCode(8);

        EmailVerifyingDto emailVerifyingDto = EmailVerifyingDto.builder().code(verificationCode).status(false).build();
        redisTemplate.opsForValue().set("verify:" + email, emailVerifyingDto, 300, TimeUnit.SECONDS);
        emailUtil.sendVerificationCode(email, verificationCode);
    }

    public void verifyingCode(String email, String code) {
        EmailVerifyingDto emailVerifyingDto = (EmailVerifyingDto) redisTemplate.opsForValue().get("verify:" + email);
        if(emailVerifyingDto == null || !emailVerifyingDto.getCode().equals(code)) throw new BizException("email_verify_fail");

        log.info("[Email : {}, Code : {}] 이메일 인증이 완료되었습니다.", email, code);
        emailVerifyingDto.setStatus(true);
        redisTemplate.opsForValue().set("verify:" + email, emailVerifyingDto, 300, TimeUnit.SECONDS);
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        return IntStream.range(0, length)
                .mapToObj(i -> characters.charAt(random.nextInt(characters.length())))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
