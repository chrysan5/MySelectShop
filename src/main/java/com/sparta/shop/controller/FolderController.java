package com.sparta.shop.controller;


import com.sparta.shop.dto.FolderRequestDto;
import com.sparta.shop.dto.FolderResponseDto;
import com.sparta.shop.security.UserDetailsImpl;
import com.sparta.shop.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FolderController {
    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    //폴더 추가 -> 1개~ n개 까지 추가 가능 - 구버전
/*    @PostMapping("/folders")
    public List<Folder> addFolders(
            @RequestBody FolderRequestDto folderRequestDto, // List<String> folderNames가 json 형태로 담겨있음
            @AuthenticationPrincipal UserDetailsImpl userDetails //로긴한 사용자 정보 받아옴
    ) {
        List<String> folderNames = folderRequestDto.getFolderNames();
        User user = userDetails.getUser(); //유저정보 들어있음

        List<Folder> folders = folderService.addFolders(folderNames, user);
        return folders;
    }*/

    @PostMapping("/folders")
    public void addFolders(@RequestBody FolderRequestDto folderRequestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<String> folderNames = folderRequestDto.getFolderNames();

        folderService.addFolders(folderNames, userDetails.getUser());
    }
    
    //이 공통적인 부분을 aop를 이용한 exceptionHandler로 처리할수있다.
/*    @PostMapping("/folders")

    public ResponseEntity addFolders(
            @RequestBody FolderRequestDto folderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            List<String> folderNames = folderRequestDto.getFolderNames();
            User user = userDetails.getUser();

            List<Folder> folders = folderService.addFolders(folderNames, user);
            return new ResponseEntity(folders, HttpStatus.OK);
        } catch(IllegalArgumentException ex) {
            RestApiException restApiException = new RestApiException();
            restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
            restApiException.setErrorMessage(ex.getMessage());
            return new ResponseEntity(
                    restApiException, // HTTP body
                    HttpStatus.BAD_REQUEST); // HTTP status code
        }
    }
*/
    //이건 이 컨트롤러에서만 적용됨
    //이것도 aop이다. 모든 IllegalArgumentException에 대해 적용됨
/*
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity handleException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(ex.getMessage());
        return new ResponseEntity(restApiException, HttpStatus.BAD_REQUEST);
    }
*/


    //폴더 전체를 조회해서 관심상품의 밑에 해시테그(폴더명)를 달아줄 수 있는 기능이 있다.
    // 회원이 등록한 모든 폴더 조회(그 회원이 추가한 것에 해당하는 모든 폴더 조회) (구버전)
    /*@GetMapping("/folders")
    public List<Folder> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser()); // 해당 user가 가진 폴더 찾기!★★★★★
    }*/


    // 회원이 등록한 모든 폴더 조회
    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }

    //이부분을 글로발(전체적)로 적용할 수있다. -> GlobalExceptionHandler를 통해
    //이 컨트롤러에서 IllegalArgumentException 예외가 터졌을때 ExceptionHandler에 의해 잡아채어진다
    //파라미터로도 예외를 받아오고 있고 그 ex변수로부터 메시지를 뽑아쓰고 있다.
    /*@ExceptioHandler({IllegalArgumentException.class}) //스프링에서 예외를 처리하기위해 제공하는 어노테이션 - 컨트롤러에서 예외처리에 사용됨
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        System.out.println("FolderController.handleException");
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }*/
    //addFolder에서 중복 폴더명을 추가하여 이 예외처리를 확인해보자
    //개발자도구를 켜고 실행하면 network - response창에서 설정한 에러메시지를 확인할 수 있다

}
