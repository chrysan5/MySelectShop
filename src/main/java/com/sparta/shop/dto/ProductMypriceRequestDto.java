package com.sparta.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter //ProductServiceTest에서 사용된다
@NoArgsConstructor
public class ProductMypriceRequestDto {
    private int myprice;

    public ProductMypriceRequestDto(int myprice) {
        this.myprice = myprice;
    }

}
