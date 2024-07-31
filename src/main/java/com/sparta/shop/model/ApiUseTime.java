package com.sparta.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ApiUseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne //회원별로 시간 측정하므로 1:1 관계, (단방향)
    @JoinColumn(name = "USER_ID", nullable = false) 
    private User user;

    @Column(nullable = false)
    private Long totalTime;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long totalCount;


    public ApiUseTime(User user, Long totalTime) {
        this.user = user;
        this.totalTime = totalTime;
        this.totalCount = 1L;
    }

    public void addUseTime(Long useTime) {
        this.totalTime += useTime;
        this.totalCount += 1L;
    }
}
