//단위 테스트
/*
package com.sparta.shop.model;

import com.sparta.shop.dto.ProductRequestDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Nested
    @DisplayName("회원이 요청한 관심상품 객체 생성")
    class CreateUserProduct {
    //멤버변수로 빼서 공통적으로 모든테스트에서 씀
        private Long userId;
        private String title;
        private String image;
        private String link;
        private int lprice;

        @BeforeEach //모든테스트(@Test)가 이걸 한번씩 거친다.
        void setup() {
            userId = 100L; //회원아이디가 100l
            title = "오리온 꼬북칩 초코츄러스맛 160g";
            image = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
            link = "https://search.shopping.naver.com/gate.nhn?id=24161228524";
            lprice = 2350;
        }

        @Test
        @DisplayName("정상 케이스") //------------------------------------------------------------
        void createProduct_Normal() {
// given - 생성자에서 this는 생략됨
            ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
            // when -> 테스트 하기위한 부분 -> 생성자 함수를 테스트 하겠다.
            Product product = new Product(requestDto, userId); //넘겨줘야 되는 값을 given에서 설정함
// then
//        생성자 함수에 돌아가고 나서의 결과가, product 객체에서 userId를 뽑아냈을 때, 그게 우리가 처음 정한
//        userId와 같은지 assertEquals((Double) expected, actual); 같은 형식임
            assertNull(product.getId());
            assertEquals(userId, product.getUserId());
            assertEquals(title, product.getTitle());
            assertEquals(image, product.getImage());
            assertEquals(link, product.getLink());
            assertEquals(lprice, product.getLprice());
            assertEquals(0, product.getMyprice());

        }

        @Nested //테스트결과를 묶어서 보여줌
        @DisplayName("실패 케이스") //---------------- edge 케이스를 고려한 단위테스트 -----------------------
        class FailCases {
            @Nested
            @DisplayName("회원 Id")
            class userId {

                @Test
                @DisplayName("null")
                void fail1() {
// given
                    userId = null; //멤버변수 설정값을 null로 바꿈
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    //expected에 쓰인 메시지는
                    assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("마이너스")
                void fail2() {
// given
                    userId = -100L;
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
                    //exception.getMessage()는 product에서부터 온 것
                }
            }

            @Nested
            @DisplayName("상품명")
            class Title {
                @Test
                @DisplayName("null")
                void fail1() {
// given
                    title = null;
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("저장할 수 있는 상품명이 없습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("빈 문자열")
                void fail2() {
// given
                    String title = "";
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("저장할 수 있는 상품명이 없습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 이미지 URL")
            class Image {
                @Test
                @DisplayName("null")
                void fail1() {
// given
                    image = null;
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("상품 이미지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("URL 포맷 형태가 맞지 않음")
                void fail2() {
// given
                    image = "shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("상품 이미지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 최저가 페이지 URL")
            class Link {
                @Test
                @DisplayName("null")
                void fail1() {
// given
                    link = "https";
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("상품 최저가 페이지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("URL 포맷 형태가 맞지 않음")
                void fail2() {
// given
                    link = "https";
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("상품 최저가 페이지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 최저가")
            class LowPrice {
                @Test
                @DisplayName("0")
                void fail1() {
// given
                    lprice = 0;
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("상품 최저가가 0 이하입니다.", exception.getMessage());
                }

                @Test
                @DisplayName("음수")
                void fail2() {
// given
                    lprice = -1500;
                    ProductRequestDto requestDto = new ProductRequestDto(title, link, image, lprice);
// when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
// then
                    assertEquals("상품 최저가가 0 이하입니다.", exception.getMessage());
                }
            }
        }
    }
}*/
