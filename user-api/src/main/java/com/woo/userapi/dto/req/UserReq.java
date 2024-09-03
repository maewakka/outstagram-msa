package com.woo.userapi.dto.req;

import com.woo.userapi.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class UserReq {
    @Email(message = "이메일 양식이 올바르지 않습니다.")
    @NotBlank(message = "빈 항목이 존재합니다.")
    private String email;

    @NotBlank(message = "빈 항목이 존재합니다.")
    private String password;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호의 양식이 올바르지 않습니다. 01x-xxx(x)-xxxx")
    @NotBlank(message = "빈 항목이 존재합니다.")
    private String phone;

    @NotBlank(message = "빈 항목이 존재합니다.")
    private String nickname;

    public User toEntity(String encodingPassword) {
        return User.builder()
                .email(email)
                .password(encodingPassword)
                .profileImgUrl("img/profileImage/default_profile.png")
                .phone(phone)
                .nickname(nickname)
                .build();
    }
}
