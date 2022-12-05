package com.main10.domain.reserve.entity;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class TimeStatus {

    @Id
    @Column(name = "TIME_STATUS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer startTime;
    private Integer endTime;
    private Integer spaceCount = 0;
    private boolean isFull;


    @ManyToOne
    @JoinColumn(name = "HOSTING_TIME_ID")
    private HostingTime hostingTime;


    @Builder
    public TimeStatus(Integer startTime,
                      Integer endTime,
                      Integer spaceCount) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.spaceCount = spaceCount;
        this.isFull = false;
    }


    public void addSpaceCount() {
        spaceCount++;
    }

    public void reduceSpaceCount() {
        spaceCount--;
    }

    public void setIsFull() {
        isFull = true;
    }

    public void setIsNotFull() {
        isFull = false;
    }

    public TimeStatus(HostingTime hostingTime, Integer startTime, Integer endTime) {
        this.hostingTime = hostingTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
