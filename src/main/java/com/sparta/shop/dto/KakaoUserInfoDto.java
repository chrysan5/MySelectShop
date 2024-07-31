package com.sparta.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String email;

    public KakaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}


//KaKaoUserInfoDto + KakaoUserService + userService = UserService가 3개로 나눠졌는데
//kakaoUserService가 리팩토링하면서 제대로 동작안해서 다시 없앰