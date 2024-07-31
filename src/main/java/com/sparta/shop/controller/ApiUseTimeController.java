package com.sparta.shop.controller;

import com.sparta.shop.model.ApiUseTime;
import com.sparta.shop.model.UserRoleEnum;
import com.sparta.shop.repository.ApiUseTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiUseTimeController {
    private ApiUseTimeRepository apiUseTimeRepository;

    @Autowired
    public ApiUseTimeController(ApiUseTimeRepository apiUseTimeRepository) {
        this.apiUseTimeRepository = apiUseTimeRepository;
    }

    //엔티티를 바로 반환하는 것보다 dto를 반환하는게 더 좋으므로 아래꺼 쓰자!
    @Secured(UserRoleEnum.Authority.ADMIN) //관리자만 조회가능
    @GetMapping("/api/use/time")
    public List<ApiUseTime> getAllApiUseTime() {
        return apiUseTimeRepository.findAll();
    }

/*    @Secured(UserRoleEnum.Authority.ADMIN) //관리자만 조회가능
    @GetMapping("/api/use/time")
    public List<ApiUseTimeDto> getAllApiUseTime() {
        List<ApiUseTime> apiUseTimeList = apiUseTimeRepository.findAll();

        List<ApiUseTimeDto> apiUseTimeDtos = new ArrayList();
        for (ApiUseTime apiUseTime : apiUseTimeList){
            ApiUseTimeDto apiUseTimeDto = new ApiUseTimeDto();
            apiUseTimeDto.setUsername(apiUseTime.getUser().getUsername());
            apiUseTimeDto.setTotalTime(apiUseTime.getTotalTime());
            apiUseTimeDtos.add(apiUseTimeDto);
        }
        return apiUseTimeDtos;
    }*/
}
