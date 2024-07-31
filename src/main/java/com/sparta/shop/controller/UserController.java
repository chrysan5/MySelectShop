package com.sparta.shop.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.shop.dto.SignupRequestDto;
import com.sparta.shop.dto.UserInfoDto;
import com.sparta.shop.jwt.JwtUtil;
import com.sparta.shop.model.UserRoleEnum;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.FolderService;
import com.sparta.shop.service.KakaoService;
import com.sparta.shop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final FolderService folderService;

    private final KakaoService kakaoService;

    // 회원 로그인 페이지
    @GetMapping("/user/login-page") //GET /user/login-page" 가 처리되지 않게 하기 위해 API 주소 변경. post/user/login은 로그인만
    public String login() {
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }


    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }

        userService.signup(requestDto);

        return "redirect:/api/user/login-page";
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoDto(username, isAdmin);
    }

    // 로그인 한 유저가 메인 페이지를 요청할 때 가지고있는 폴더를 반환
    @GetMapping("/user-folder")
    public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        model.addAttribute("folders", folderService.getFolders(userDetails.getUser()));

        return "index :: #fragment";
    }


//    @GetMapping("/user/kakao/callback") (구버전)
//    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException {
//// authorizedCode: 카카오 서버로부터 받은 인가 코드
//        userService.kakaoLogin(code);
//
//        return "redirect:/";
//    }


    //카카오에서 보내주는 인가코드를 받는다. ex) http://localhost:8080/user/kakao/callback?code=zAGhy36K0...
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
        String token = kakaoService.kakaoLogin(code);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

}