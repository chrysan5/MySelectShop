package com.sparta.shop.scheduler;

import com.sparta.shop.dto.ItemDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//이거대신 itemSearch 사용함

//검색을 main 메소드에서 하는게아니라 controller에서 가져다 써야하는데,
//스프링이 자동으로 필요한 클래스를 필요한 곳에 생성하게끔 컴포넌트 등록을 해서 권한을 준다.
@Component // 이제부터, @RequiredArgsConstructor 와 함께 사용할 경우 스프링이 자동으로 생성함
public class NaverShopSearch {

    public String search(String query) {

        // Controller 가 자동으로 해주는 일
        // 1. API Request 의 파라미터 값에서 검색어 추출 -> query 변수
        // 5. API Response 보내기
        //  5.1) response 의 header 설정
        //  5.2) response 의 body 설정


        //ㅡㅡㅡㅡㅡarc의 cone snippets-java-spring에서 copy해온 내용ㅡㅡㅡㅡㅡㅡ

       // 2. 네이버 쇼핑 API 호출에 필요한 Header, Body 정리
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "3GhCh4N6TAl3DQQHVW6d");
        headers.add("X-Naver-Client-Secret", "bqxy5vNPWC");
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);

        // 3. 네이버 쇼핑 API 호출 결과 -> naverApiResponseJson (JSON 형태)
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?query=" + query, HttpMethod.GET, requestEntity, String.class);
        //HttpStatus httpStatus = responseEntity.getStatusCode();
        HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
        System.out.println("Response status: " + status);
        System.out.println(response);

        return response;
    }

    //org.json 패키지를 메이븐에서 설치함 (build.gradle > dependencies)
    public List<ItemDto> fromJSONtoItems(String result) {
        // 4. naverApiResponseJson (JSON 형태) -> itemDtoList (자바 객체 형태)
        //  - naverApiResponseJson 에서 우리가 사용할 데이터만 추출 -> List<ItemDto> 객체로 변환

        JSONObject rjson = new JSONObject(result); //문자열(result)정보를 json 객체로 바꾸기
        JSONArray items  = rjson.getJSONArray("items");  //{"total":73580,...,"items":[{"cate":"PC","image":"htt-p...jpg","mName":"네이버"}]등 아이템이 리스트므로
        List<ItemDto> ret = new ArrayList<>();
        for (int i=0; i<items.length(); i++) { //JSONArray는 length()
            JSONObject itemJson = items.getJSONObject(i);
            System.out.println(itemJson);
//              String title = itemJson.getString("title");
//              String image = itemJson.getString("image");
//              int lprice = itemJson.getInt("lprice");
////            String link = itemJson.getString("link");
            ItemDto itemDto = new ItemDto(itemJson); //위에 4줄대신 쓸수있다.
            ret.add(itemDto);
        }
        return ret;
    }

    public static void main(String[] args) { //이건 테스트 코드
        NaverShopSearch naverShopSearch = new NaverShopSearch();
        String ret = naverShopSearch.search("아이맥");
        naverShopSearch.fromJSONtoItems(ret);
    }
}
