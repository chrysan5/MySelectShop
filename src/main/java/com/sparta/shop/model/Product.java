package com.sparta.shop.model;

import com.sparta.shop.dto.ItemDto;
import com.sparta.shop.dto.ProductMypriceRequestDto;
import com.sparta.shop.dto.ProductRequestDto;
import com.sparta.shop.validator.ProductValidator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@Entity
//public class Product extends Timestamped{ //타임스탬프 적용하는 방법 나중에 배우기
public class Product{

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO) //db에 맞게 자동으로 조정한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "product")
    private List<ProductFolder> productFolderList = new ArrayList<>();


    public Product(ProductRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
    }

    // 관심 상품 생성 시 이용
    public Product(ProductRequestDto requestDto, User user) {
        // 입력값 Validation -ProductValidator을 new로 생성하지않고 static으로 만들어서 바로 가져다 쓴다
        ProductValidator.validateProductInput(requestDto, user);

        this.user = user;
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.myprice = 0;
    }

    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice();
    }

    public void update(ProductMypriceRequestDto requestDto) {
        this.myprice = requestDto.getMyprice();
    }
}




