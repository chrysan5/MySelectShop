package com.sparta.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling // 스프링 부트에서 스케줄러가 작동하게 합니다. scheduler 와 관련
//@EnableJpaAuditing // 시간 자동 변경이 가능하도록 합니다. -> 테스트소스 오류나므로 주석처리 후 JpaConfig 클래스 만들었다.
@SpringBootApplication // 스프링 부트임을 선. componuntScan 이미 설정되어 있다
public class MySelectShop {

	public static void main(String[] args) {
		SpringApplication.run(MySelectShop.class, args);
	}

}


