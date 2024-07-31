package com.sparta.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//(구버전)
//global 형태 => 모든 곳에 적용됨
/*@RestControllerAdvice //controller+ResponseBody => json형태로 restApiException(바디부분)을 넘겨줌
//RestControllerAdvice는 클래스 레벨의 어노테이션. 이 어노테이션 사용시 모든 컨트롤러에서 발생하는 예외처리를 이곳으로 잡아올 수 있다
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(ex.getMessage());

        return new ResponseEntity(restApiException, HttpStatus.BAD_REQUEST);
    }
}*/


@RestControllerAdvice //예외처리를 위한 클래스레벨 에노테이션
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>( //프론트의 response로 보내주는 응답을 설정
                // HTTP body
                restApiException, //이렇게 하면 json 형태로 반환 할 수 있다.
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<RestApiException> nullPointerExceptionHandler(NullPointerException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<RestApiException> notFoundProductExceptionHandler(ProductNotFoundException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.NOT_FOUND
        );
    }
}