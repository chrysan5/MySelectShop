package com.sparta.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private String title;
    private String link;
    private String image;
    private int lprice; //최저가

}