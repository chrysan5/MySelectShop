package com.sparta.shop.scheduler;


import com.sparta.shop.dto.ItemDto;
import com.sparta.shop.model.Product;
import com.sparta.shop.repository.ProductRepository;
import com.sparta.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.TimeUnit;



@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final NaverShopSearch naverShopSearch;

    //매일 새벽 1시에 관심 상품 목록 제목으로 검색헤서, 최저가를 업뎃하는 스케줄러.

    // 초, 분, 시, 일, 월, 주 순서/ *=뭐든 상관없다/cron= 정해진 시각

    @Scheduled(cron = "0 0 1 * * *") //새벽한시
    //@Scheduled(cron = "10/* * * * * *") //프로젝트 run하자마자 테스트는 이걸로 하면 됨
    public void updatePrice() throws InterruptedException {
        System.out.println("가격 업데이트 실행");

        List<Product> productList = productRepository.findAll(); // 저장된 모든 관심상품을 조회합니다.
        for (int i=0; i<productList.size(); i++) {
            TimeUnit.SECONDS.sleep(1); //1초에 한번씩 for문을 돌게됨(조회함)(Naver 제한)
            Product p = productList.get(i); // i 번째 관심 상품을 꺼냅니다.
            String title = p.getTitle(); // i 번째 관심 상품의 제목으로 검색을 실행합니다.
            String resultString = naverShopSearch.search(title);
            // i 번째 관심 상품의 검색 결과 목록 중에서 첫 번째 결과를 꺼냅니다.
            List<ItemDto> itemDtoList = naverShopSearch.fromJSONtoItems(resultString);
            ItemDto itemDto = itemDtoList.get(0); //제일 위에가 내가 원하는 녀석이므로
            Long id = p.getId(); // i 번째 관심 상품 정보를 업데이트합니다.
            productService.updateBySearch(id, itemDto); //updateBySearch는 ProductService의 함수이다.
        }
    }
}