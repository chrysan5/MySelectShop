//package com.sparta.week04;
//
//import com.sparta.week04.models.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
////그냥 개념적 설명 페이지이다.
//
//
//@Configuration //스프링이 젤첨 기동될때 이부분을 읽는다.
////이건 @Componunt로 못함 -왜냐면 처음 생성되는 객체므로..
////productRepository를 쓰는 productRepository, productRepository를 쓰는 productSevice에 componunt를 붙여준다
////컴포넌트써주면 자동으로 객체를 생성해준다.
////@Bean을 쓰려면 @Autowired쓰면 스프링에 의해 DI(의존성주입)됨
//public class BeanConfiguration {
//
//    @Bean //빈이란 스프링이 관리하는 객체. ->여기서 리턴되는 값을 빈에 등록하겠다
//    public ProductRepository productRepository() {
//        String dbUrl = "jdbc:h2:mem:springcoredb";
//        String dbId = "sa";
//        String dbPassword = "";
//
//        return new ProductRepository(dbUrl, dbId, dbPassword);
//    }
//}
//
//////-----------------예시----------------
////@Component
////public class  ProductService{
////    @Autowired (-> 생략조건: spring4.3 이후부터 생성자 선언이 1개일때만 생략 가능하다.)
////    private  ProductRepository productRepository;
////}
//
////@RequiredArgsConstructor 은 멤머변수중 final로 설정된 애들의 생성자를 대신 만들어준다.
