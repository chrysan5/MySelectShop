package com.sparta.shop.security;


import com.sparta.shop.model.User;
import com.sparta.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    //@RequiredArgsConstructor
   @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        return new UserDetailsImpl(user);
    }

}
//3. UserDetails 를 **"인증 관리자"**에게 전달(자동 by security)
//
//4. "인증 관리자" 가 **인증 처리**
//    1. 아래 2 개의 username, password 일치 여부 확인
//        1. Client 가 로그인 시도한 username, password
//        2. UserDetailsService 가 전달해준 UserDetails 의 username, password
//    2. password 비교 시
//        1. Client 가 보낸 password 는 평문이고, UserDetails 의 password 는 암호문
//        2. Client 가 보낸 password 를 암호화해서 비교
//    3. 인증 성공 시 → 세션에 로그인 정보 저장
//    4. 인증 실패 시 →  Error 발생
