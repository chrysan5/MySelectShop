package com.sparta.shop.validator;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLValidator {
    public static boolean isValidUrl(String url)
    {
        try {
            new URL(url).toURI(); //URL이라는 내장함수가 url을 url형태로 만들어준다. url형태가 아닐경우 알아서 아래의 에러를 내줌
            return true;
        }
        catch (URISyntaxException exception) {
            return false;
        }
        catch (MalformedURLException exception) {
            return false;
        }
    }
}