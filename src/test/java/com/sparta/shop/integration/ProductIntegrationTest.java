
package com.sparta.shop.integration;

//@Test : 이 메소드가 test method임을 명시해줌으로써 ide에서 테스트를 진행할 때, 문제되는 test method는 무엇인지, 혹은 몇개인지 알려줄 수
// 있을 뿐 아니라, 각종 테스트 기능을 제공해줄 수 있게 된다.

//@Rollback(false) :테스트 중에 db에 입력된 값을 테스트 전 원래상태로 rollback해주는 어노테이션이다. Default값으로 true이며,
// 나는 db에 직접 값이 들어가는지 확인하기 위해 false로 지정해주었다.test code에서 확인 뿐만 아니라, db에서도 확인하고 싶었기 때문이다.

//@SpringBootTest : 스프링부트 어플리케이션 테스트를 실행할 때 필요한 대부분의 의존성을 제공해준다. @SpringBootApplication을 실행하면서
// 스프링 빈 등록 및 의존성 관련 어노테이션 기능들을 제공해주므로 Spring boot Application으로 Test를 작성하고 싶으면 그냥 무조건 붙여준다고 보면 될 듯 하다.



////밑에꺼가 정렬 추가한 버전이다
//다른 애플리케이션 돌리고있으면 테스트할때 에러나므로 렌덤포트 해줌
/*@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //이걸해주면 db까지 접근해서 스프링 이용가능함(빈 이용가능)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductIntegrationTest {
    @Autowired //원래 단위테스트는 빈못써서 안되지만 통합테슽에서는 오토와이어드로 빈을 di함
    ProductService productService;

    Long userId = 100L;
    ProductResponseDto createdProduct = null;
    int updatedMyPrice = -1;

    @Test
    @Order(1)
    @DisplayName("신규 관심상품 등록")
    void test1() {
// given
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );

// when
        ProductResponseDto product = productService.createProduct(requestDto, user);

// then
        assertNotNull(product.getId()); //여기서 assert햇ㄴ는데 아래에서 왜 createdProduct가 null?? (3-10강의)
//        system.out.println(product.getId()); system안됨.
        //assertEquals(userId, product.getUserId()); product를 dto에 담으면서 불가능해짐
        assertEquals(title, product.getTitle());
        assertEquals(imageUrl, product.getImage());
        assertEquals(linkUrl, product.getLink());
        assertEquals(lPrice, product.getLprice());
        assertEquals(0, product.getMyprice());
        createdProduct = product; //this.createdProduct에서 this가 생략된것임 .order2에서 써주기위해 변수생성
    }

    @Test
    @Order(2)
    @DisplayName("신규 등록된 관심상품의 희망 최저가 변경")
    void test2() {
// given
        Long productId = this.createdProduct.getId(); //이게 null떠서 에러임
        int myPrice = 70000;
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto(myPrice);

// when
        ProductResponseDto product = productService.updateProduct(productId, requestDto);

// then
        assertNotNull(product.getId());
        //assertEquals(userId, product.getUserId()); product를 dto에 담으면서 불가능해짐
        assertEquals(this.createdProduct.getTitle(), product.getTitle());
        assertEquals(this.createdProduct.getImage(), product.getImage());
        assertEquals(this.createdProduct.getLink(), product.getLink());
        assertEquals(this.createdProduct.getLprice(), product.getLprice());
        assertEquals(myPrice, product.getMyprice());
        this.updatedMyPrice = myPrice;
    }

    @Test
    @Order(3)
    @DisplayName("회원이 등록한 모든 관심상품 조회")
    void test3() {
// given
        int page = 0;
        int size = 10;
        String sortBy = "id";
        boolean isAsc = false;

// when
        Page<Product> productList = productService.getProducts(userId, page, size, sortBy, isAsc);

// then
// 1. 전체 상품에서 테스트에 의해 생성된 상품 찾아오기 (상품의 id 로 찾음)
        Long createdProductId = this.createdProduct.getId();
        Product foundProduct = productList.stream()
                .filter(product -> product.getId().equals(createdProductId)) //전체를 돌면서 product하나를 찾아온다
                .findFirst()
                .orElse(null);

// 2. Order(1) 테스트에 의해 생성된 상품과 일치하는지 검증
        assertNotNull(foundProduct);
        assertEquals(userId, foundProduct.getUserId());
        assertEquals(this.createdProduct.getId(), foundProduct.getId());
        assertEquals(this.createdProduct.getTitle(), foundProduct.getTitle());
        assertEquals(this.createdProduct.getImage(), foundProduct.getImage());
        assertEquals(this.createdProduct.getLink(), foundProduct.getLink());
        assertEquals(this.createdProduct.getLprice(), foundProduct.getLprice());

// 3. Order(2) 테스트에 의해 myPrice 가격이 정상적으로 업데이트되었는지 검증
        assertEquals(this.updatedMyPrice, foundProduct.getMyprice());
    }
}
*/
