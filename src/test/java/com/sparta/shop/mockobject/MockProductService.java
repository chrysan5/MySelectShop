/*
package com.sparta.shop.mockobject;

import com.sparta.shop.dto.ProductMypriceRequestDto;
import com.sparta.shop.dto.ProductRequestDto;
import com.sparta.shop.model.Product;

import java.util.List;

//service 테스트시는 가짜객체 필요!
//실제 ProductService와 구성은 동일 - 가짜 객체만드는 곳(주입 피하기위해)과,
//그걸 이용하는 ProductService-> MockProductService 만 바뀜

public class MockProductService {

    private final MockProductRepository mockProductRepository;
    public static final int MIN_MY_PRICE = 100;

    //가짜 객체 만들기
    public MockProductService() {
        this.mockProductRepository = new MockProductRepository();
    }

    public Product createProduct(ProductRequestDto requestDto, Long userId ) {
// 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto, userId);

        mockProductRepository.save(product);

        return product;
    }

    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.");
        }

        Product product = mockProductRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));

        product.setMyprice(myprice);
        mockProductRepository.save(product);

        return product;
    }

    //이 부분이 정렬한다고 업데이트 되어서 바꿔줘야한다
    // 회원 ID 로 등록된 상품 조회
    public List<Product> getProducts(Long userId) {
        return mockProductRepository.findAllByUserId(userId);
    }
    //이것도 마찬가지
    // (관리자용) 상품 전체 조회
    public List<Product> getAllProducts() {
        return mockProductRepository.findAll();
    }
}*/
