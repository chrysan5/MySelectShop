package com.sparta.shop.controller;


import com.sparta.shop.dto.ProductMypriceRequestDto;
import com.sparta.shop.dto.ProductRequestDto;
import com.sparta.shop.dto.ProductResponseDto;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController // JSON으로 데이터를 주고받음을 선언합니다.
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    //private final ApiUseTimeRepository apiUseTimeRepository;


    //return값을 Product가 아니라 dto로 받는 구조가 더 좋다.
    // 관심 상품 등록하기
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 응답 보내기
        return productService.createProduct(requestDto, userDetails.getUser());
    }


    // 신규 상품 등록 - apiUseTime 계산 적용시. -> 모든 메서드마다 이렇게 처리해야하므로 스프링의 aop기능을 이용해야 한다.
    /*@PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        long startTime = System.currentTimeMillis(); // 측정 시작 시간
        try {
            // 응답 보내기
            // 수행시간 = 종료 시간 - 시작 시간
            return productService.createProduct(requestDto, userDetails.getUser());

        } finally {
            long endTime = System.currentTimeMillis(); // 측정 종료 시간
            long runTime = endTime - startTime;  // 수행시간 = 종료 시간 - 시작 시간

            User loginUser = userDetails.getUser(); // 로그인 회원 정보
            ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser) // API 사용시간 및 DB 에 기록
                    .orElse(null);
            if (apiUseTime == null) { // 로그인 회원의 기록이 없으면
                apiUseTime = new ApiUseTime(loginUser, runTime);
            } else { // 로그인 회원의 기록이 이미 있으면
                apiUseTime.addUseTime(runTime);
            }
            System.out.println("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
            apiUseTimeRepository.save(apiUseTime);
        }
    }*/



    //관심상품의 희망 최저가 등록하기
    @PutMapping("/products/{id}") //여기서 id는 product의 id 이다. 이 id는 PathVariable 괄호안 변수와 동일해야 하지만, 클라에서 보내는 변수명과는 달라도 된다.
    public ProductResponseDto updateProduct(@PathVariable("id") Long productId, //uri 경로 변수 이름이 @PathVariable 어노테이션의 괄호 안에 들어가는 변수 이름과 동일해야 한다.
                                            @RequestBody ProductMypriceRequestDto requestDto) {
        return productService.updateProduct(productId, requestDto);
    }


    // 관심 상품 조회하기
    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
            @RequestParam("page") int page, //프론트로부터 받아오는 page 값은 1부터시작
            @RequestParam("size") int size, //size : 한 페이지에 보여줄 상품 개수
            @RequestParam("sortBy") String sortBy, //정렬항목(ex-id, title, lprice, createdAt)
            @RequestParam("isAsc") boolean isAsc, //true일경우 오름차순, false인 경우 내림차순
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 응답 보내기
        return productService.getProducts(userDetails.getUser(),  page-1, size, sortBy, isAsc);
        ////서버의 페이지는 0부터시작하므로 page-1 해줌
    }
    //서버에서 클라이언트로 보내는 값들 -> 스프링이 알아서 보내줌(페이지 검사로 보면 나온다)
 /* - number: 조회된 페이지 번호 (**0부터 시작**)
    - content: 조회된 상품 정보 (product 정보들이 배열로 나감)
    - size: 한 페이지에 보여줄 상품 개수
    - numberOfElements: 실제 조회된 상품 개수(마지막 페이지는 size 크기와 다르므로 이값이 필요)
    - totalElement: 전체 상품 개수 (회원이 등록한 모든 상품의 개수)
    - totalPages: 전체 페이지 수 (totalPages = totalElement / size 결과를 소수점 올림)
        - first: 첫 페이지인지? (boolean)
        - last: 마지막 페이지인지? (boolean)*/



    // 상품에 폴더 추가
    @PostMapping("/products/{productId}/folder")
    public void addFolder(
            @PathVariable("productId") Long productId, //productId는 uri로 받음 - PathVariable는 uri로 받을때 쓰는 거임
            @RequestParam("folderId") Long folderId, //folderId는 form 형태로 받음 - RequestParam는 get,post 두가지 방식으로 받을 수 있음
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.addFolder(productId, folderId, userDetails.getUser());
    }


    // 회원이 등록한 폴더 내 모든 상품 조회 (회원이 등록한 폴더선택시 관심상품들 나오게 조회)
    @GetMapping("/folders/{folderId}/products") //Page<Product>를 출력해줘야 한다
    public Page<ProductResponseDto> getProductsInFolder(
            @PathVariable("folderId") Long folderId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        //forderService, productService는 판단에 따라 사용하면됨. 여기서는 이걸로 쓸거다.
        return productService.getProductsInFolder(
                folderId,
                page-1,
                size,
                sortBy,
                isAsc,
                userDetails.getUser()
        );
    }
}