package com.sparta.shop.mvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.shop.config.WebSecurityConfig;
import com.sparta.shop.controller.ProductController;
import com.sparta.shop.controller.UserController;
import com.sparta.shop.dto.ProductRequestDto;
import com.sparta.shop.model.User;
import com.sparta.shop.model.UserRoleEnum;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.FolderService;
import com.sparta.shop.service.KakaoService;
import com.sparta.shop.service.ProductService;
import com.sparta.shop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//mvc테스트는 컨트롤러를 테스트 ->http 통신이 필요함 (브라우저와 통신)
//user,product를 원래 나눠야하지만 합쳐놓음

//이거 테스트안됨.....


//@WebMvcTest : 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션입니다.
// 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있습니다. 단, @Service, @Component, @Repository 등은 사용할 수 없다.


@WebMvcTest(
        controllers = {UserController.class, ProductController.class}, //테스트할 컨트롤러 지정
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserProductMvcTest {
    private MockMvc mvc; //목(가짜) 객체

    private Principal mockPrincipal; //가짜 인증객체

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean //가짜 빈 - UserController에 있는 것들을 똑같이 주입받을 수 있도록함
    UserService userService;

    @MockBean
    KakaoService kakaoService;

    @MockBean
    ProductService productService;

    @MockBean
    FolderService folderService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "sollertia4351";
        String password = "robbie1234";
        String email = "sollertia@sparta.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password, email, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
        //여기 토큰안에 UserDetailsImpL이 잇음 -그 안에 user가 있음
    }

    @Test
    @DisplayName("로그인 Page")
    void test1() throws Exception {
        // when - then
        mvc.perform(get("/api/user/login-page"))
                .andExpect(status().isOk()) //내장함수 isOk
                .andExpect(view().name("login")) //login.html 반환함을 예측
                .andDo(print()); //http에서의 header, body를 print해줌
    }
    //이대로 테스트 런할경우
    //org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaAuditingHandler': Cannot resolve reference to bean 'jpaMappingContext' while setting constructor argument
    //-> jpa 관련된 오류가 뜨는데 이 이유는 MySelectShop의 @EnableJpaAuditing 때문이므로 이를 주석처리하고
    //JpaConfig라는 클래스를 만들어주었다.



    @Test
    @DisplayName("회원 가입 요청 처리")
    void test2() throws Exception {
        // given -컨트롤러의 registorUser함수의 signupRequestDto의 form을 만들어주는것임
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
        signupRequestForm.add("username", "sollertia4351");
        signupRequestForm.add("password", "robbie1234");
        signupRequestForm.add("email", "sollertia@sparta.com");
        signupRequestForm.add("admin", "false");

        // when - then
        mvc.perform(post("/api/user/signup")
                        .params(signupRequestForm)
                )
                .andExpect(status().is3xxRedirection()) //redirect하면 3xx나와야하므로
                .andExpect(view().name("redirect:/api/user/login-page"))
                //컨트롤러의 return값이 view name임
                .andDo(print());
    }

    @Test
    @DisplayName("신규 관심상품 등록")
    void test3() throws Exception {
        // given
        this.mockUserSetup(); //가짜유저를 만들어줌(그래야 상품등록되므로)
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

        String postInfo = objectMapper.writeValueAsString(requestDto); //objectMapper는 json 형태를담은 스트링을 만들어줌

        // when - then
        mvc.perform(post("/api/products")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
