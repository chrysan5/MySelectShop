package com.sparta.shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;


//요청한 데이터 결과를 브라우저에 돌려주려면 데이터를 들고나르는 ItemDto 클래스 필요
@Getter //setter는 생성자를 통해서만 쓸거므로 없다
@NoArgsConstructor
public class ItemDto {
   private String title;
    private String link;
    private String image;
    private int lprice;

    public ItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.link = itemJson.getString("link");
        this.image = itemJson.getString("image");
        this.lprice = itemJson.getInt("lprice");
    }
}

//dto는 클라이언트로부터 온 요청들을 모두 .dto로 갖고있음
//왜냐면 우리는 entity(product 테이블)을 대부분 바로 클라이언트에게 보내지않고
//    dto로 변환해서 보내주는게 일반적이기때문
//        왜냐면 db의 내용이 클라이언트에게 보내야 될 내용과 다를수잇으므로
//        클라이언트에게 보내는 responseDto로 변환해서 보냄
