package com.sparta.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Folder {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //폴더네임

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false) //db 컬럼명
    private User user;
    //객체가 객체를 갖고있을때 => has-a 관계라고 함
    //-nullable -> **false** (default)
    //    - 예) 폴더는 회원에 의해서만 만들어짐. user 값이 필수
    //- **true**
    //    - 예) 공용폴더의 경우, 폴더의 user 객체를 null 로 설정하기로 함

    public Folder(String name, User user) {
        this.name = name; //폴더명
        this.user = user; //유저정보
    }
}

//--설계--
//폴더 별로 관심상품을 저장/관리할 수 있는 기능을 추가하기로 함
//폴더 생성 : 회원별로 폴더 생성, 이때 한번에 1~n개를 추가할 수 있다.
//관심상품에 폴더 설정 : 관심상품이 등록되는 시점에는 어느 폴더에도 저장되지 않는다. 이후에 폴더에 등록가능
//=> 폴더별로 관심상품 조회가 가능함(전체/폴더명)
//그리고 상품별로 폴더 추가가 가능함 ex-나이키신발 #신발 #옷 (신발, 옷이 폴더명임)
//상품과 폴더는 n:m의 관계이다