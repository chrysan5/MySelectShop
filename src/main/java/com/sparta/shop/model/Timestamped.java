package com.sparta.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 멤버 변수가 컬럼이 되도록 합니다.
@EntityListeners(AuditingEntityListener.class) // 변경되었을 때 자동으로 기록합니다.
public abstract class Timestamped {

    @CreatedDate // 최초 생성 시점
    @Column(updatable = false) //최초에만 업데이트
    @Temporal(TemporalType.TIMESTAMP) //timestamp 형식으로 변경해줌
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 변경 시점
    @Column //얘는 계속 업데이트됨
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;
}