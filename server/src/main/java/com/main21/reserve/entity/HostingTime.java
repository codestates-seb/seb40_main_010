package com.main21.reserve.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HostingTime {

    @Id
    @Column(name = "HOSTING_TIME_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date currentDate;

    @OneToMany(mappedBy = "hostingTime", cascade = CascadeType.ALL)
    List<TimeStatus> timeStatuses = new ArrayList<>();


    private Long placeId;

    @Builder
    public HostingTime (Date currentDate,
                        Long placeId) {
        this.currentDate = currentDate;
        this.placeId = placeId;
    }
}
