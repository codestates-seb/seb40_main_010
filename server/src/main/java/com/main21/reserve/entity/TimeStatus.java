package com.main21.reserve.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeStatus {

    @Id
    @Column(name = "TIME_STATUS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer startTime;
    private Integer endTime;
    private Integer spaceCount;
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

    public void setIsFull() {
        isFull = true;
    }
}
