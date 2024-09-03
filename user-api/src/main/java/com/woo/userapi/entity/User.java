package com.woo.userapi.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "OUTSTAGRAM_USER")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "OUTSTAGRAM_USER_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="OUTSTAGRAM_USER_SEQUENCE_GENERATOR", sequenceName = "OUTSTAGRAM_USER_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String nickname;
    private String phone;

    @Column(name = "profile_img_url")
    private String profileImgUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public User(String email, String password, String nickname, String phone, String profileImgUrl, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
    }
}
