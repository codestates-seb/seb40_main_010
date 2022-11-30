package com.main10.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_IMAGE_ID")
    private Long id;


    private String originFileName;


    private String fileName;


    private String filePath;


    private Long fileSize;


    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @Builder
    public MemberImage(Long id,
                       String originFileName,
                       String fileName,
                       String filePath,
                       Long fileSize) {
        this.id = id;
        this.originFileName = originFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }


    public void setMember(Member member) {
        this.member = member;
    }
}
