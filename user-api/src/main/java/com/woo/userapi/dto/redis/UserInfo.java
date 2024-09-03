package com.woo.userapi.dto.redis;

import com.woo.userapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo implements Serializable {

    private Long id;
    private String email;
    private String nickName;
    private String phone;
    private String profileImgUrl;

    public static UserInfo of(User user, String profileImgUrl) {
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .phone(user.getPhone())
                .profileImgUrl(profileImgUrl)
                .build();
    }
}
