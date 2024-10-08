package com.sparta.shop.validator;

import com.sparta.shop.dto.ProductRequestDto;
import com.sparta.shop.model.User;
import org.springframework.stereotype.Component;
// 입력값 Validation - edge케이스 위한 단위 테스트위해


@Component
public class ProductValidator {
    public static void validateProductInput(ProductRequestDto requestDto, User user) {
        if (user == null) {
            throw new IllegalArgumentException("회원 Id 가 유효하지 않습니다.");
        }

        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("저장할 수 있는 상품명이 없습니다.");
        }

        if (!URLValidator.isValidUrl(requestDto.getImage())) {
            throw new IllegalArgumentException("상품 이미지 URL 포맷이 맞지 않습니다.");
        }

        if (!URLValidator.isValidUrl(requestDto.getLink())) {
            throw new IllegalArgumentException("상품 최저가 페이지 URL 포맷이 맞지 않습니다.");
        }

        if (requestDto.getLprice() <= 0) {
            throw new IllegalArgumentException("상품 최저가가 0 이하입니다.");
        }
    }
}