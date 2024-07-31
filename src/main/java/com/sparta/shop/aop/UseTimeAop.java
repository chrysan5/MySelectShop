package com.sparta.shop.aop;

import com.sparta.shop.model.ApiUseTime;
import com.sparta.shop.model.User;
import com.sparta.shop.repository.ApiUseTimeRepository;
import com.sparta.shop.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "UseTimeAop")
@Aspect //aop 클래스에 이것 적어두면 된다
@Component //여기 콤포넌트 써줬음(빈으로 등록)
public class UseTimeAop {

    private final ApiUseTimeRepository apiUseTimeRepository;

    public UseTimeAop(ApiUseTimeRepository apiUseTimeRepository) {
        this.apiUseTimeRepository = apiUseTimeRepository;
    }

    @Pointcut("execution(* com.sparta.shop.controller.ProductController.*(..))")
    private void product() {} //위의 포인트컷을 쓰려면 이 메서드명만 가져다 쓰면 된다
    @Pointcut("execution(* com.sparta.shop.controller.FolderController.*(..))")
    private void folder() {}
    @Pointcut("execution(* com.sparta.shop.controller.ItemSearchController.*(..))")
    private void naver() {}

    //원래 DispatcherServlet과 Controller를 거치지만, 그 사이에
    //AOP Proxy와 @Around Advice를 거치게 된다.

    //어드바이스 종류류
/*   - @Around: '핵심기능' 수행 전과 후 (@Before + @After)
    - @Before: '핵심기능' 호출 전 (ex. Client 의 입력값 Validation 수행)
    - @After:  '핵심기능' 수행 성공/실패 여부와 상관없이 언제나 동작 (try, catch 의 finally() 처럼 동작)
    - @AfterReturning: '핵심기능' 호출 성공 시 (함수의 Return 값 사용 가능)
    - @AfterThrowing: '핵심기능' 호출 실패 시. 즉, 예외 (Exception) 가 발생한 경우만 동작
        (ex. 예외가 발생했을 때 개발자에게 email 이나 SMS 보냄)*/
    //@Around("execution(public * com.sparta.shop.controller..*(..))")

    @Around("product() || folder() || naver()") //포인트컷 사용 방법임. 수행전후에 시작,끝시간 찍어야하므로 around사용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 측정 시작 시간
        long startTime = System.currentTimeMillis();

        try { //Object는 자바의 모든 클래스를 담을 수 있음
            Object output = joinPoint.proceed(); // 핵심기능 수행 ★★★★★
            return output;
        } finally {
            long endTime = System.currentTimeMillis(); // 측정 종료 시간
            long runTime = endTime - startTime; // 수행시간 = 종료 시간 - 시작 시간

            // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                User loginUser = userDetails.getUser();

                // API 사용시간 및 DB 에 기록
                ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser).orElse(null);
                if (apiUseTime == null) {
                    // 로그인 회원의 기록이 없으면
                    apiUseTime = new ApiUseTime(loginUser, runTime);
                } else {
                    // 로그인 회원의 기록이 이미 있으면
                    apiUseTime.addUseTime(runTime);
                }

                log.info("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
                apiUseTimeRepository.save(apiUseTime);
            }
        }
    }
}