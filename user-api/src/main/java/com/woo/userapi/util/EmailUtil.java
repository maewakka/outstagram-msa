package com.woo.userapi.util;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendVerificationCode(String email, String code){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Pocket Money Guardian : 인증번호");
            String context = setCodeContext(code);
            mimeMessageHelper.setText(context, true);
            new Thread(() -> sendEmail(mimeMessage, email)).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String setCodeContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return springTemplateEngine.process("code", context);
    }

    public void sendEmail(MimeMessage mimeMessage, String email) {
        javaMailSender.send(mimeMessage);
        log.info("[Email : {}] 이메일 인증 코드가 전송 완료되었습니다.", email);
    }
}
