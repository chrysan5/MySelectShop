package com.sparta.shop.exception;

//번개모양은 exception 클래스란 의미이다.
public class ProductNotFoundException extends  RuntimeException {
    //ctrl+o : 오버라이드 가능
    public ProductNotFoundException(String message) {
        super(message);
    }
}
