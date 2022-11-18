package com.main21.member.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.file.FileHandler;
import com.main21.file.S3Upload;
import com.main21.file.UploadFile;
import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.entity.MemberImage;
import com.main21.member.repository.MemberImageRepository;
import com.main21.member.repository.MemberRepository;
import com.main21.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final FileHandler fileHandler;
    private final MemberImageRepository memberImageRepository;

    @Autowired
    private S3Upload s3Upload;


    public void createMember(MemberDto.Post post) {
        verifyEmail(post);
        List<String> roles = authorityUtils.createRoles(post.getEmail());

        Member member = Member.builder()
                .email(post.getEmail())
                .password(passwordEncoder.encode(post.getPassword()))
                .nickname(post.getNickname())
                .phoneNumber(post.getPhoneNumber())
                .roles(roles)
                .mbti(post.getMbti())
                .build();


        memberRepository.save(member);
    }
    public void updateMember(Long memberId, MemberDto.Patch patch) {
        Member findMember = findVerifyMember(memberId);
        findMember.editMember(patch.getNickname(), patch.getMbti());
        memberRepository.save(findMember);
    }

    public MemberDto.Info getMember(Long memberId) {
        Member findMember = findVerifyMember(memberId);
        MemberDto.Info memberInfo = MemberDto.Info.builder()
                //.profileImage(findMember.getMemberImage().getFilePath()) // 추후 수정
                .nickname(findMember.getNickname())
                .mbti(findMember.getMbti())
                .build();

        return memberInfo;
    }

    // 이미 존재하는 회원 파악
    public Member findVerifyMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND)); // 멤버로 수정해야함
    }

    public void verifyEmail(MemberDto.Post post) {
        if (memberRepository.findByEmail(post.getEmail()).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND); // 멤버로 바꿔야 ㅍ
        }
    }

    /**
     * 회원 프로필 사진 업로드 Local (유저검증 필요)
     */
    public void createProfile(List<MultipartFile> multipartFiles) throws Exception {

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(multipartFiles);
        List<MemberImage> memberImageList = new ArrayList<>();

        uploadFileList.forEach(uploadFile -> {
            MemberImage memberImage = MemberImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();
            memberImageList.add(memberImage);
        });

        if(!memberImageList.isEmpty()) {
            for (MemberImage memberImage : memberImageList) {
                //유저 FK 저장 관계 필요

                //파일 DB 저장
                memberImageRepository.save(memberImage);
            }
        }
    }

    /**
     * 회원 프로필 사진 업로드 S3 (유저검증 필요)
     */
    public void createProfileS3(MultipartFile file) throws Exception {
        String dir = "memberImage";

        UploadFile uploadFile = s3Upload.uploadfile(file, dir);

        MemberImage memberImage = MemberImage.builder()
                .originFileName(uploadFile.getOriginFileName())
                .fileName(uploadFile.getFileName())
                .filePath(uploadFile.getFilePath())
                .fileSize(uploadFile.getFileSize())
                .build();

        memberImageRepository.save(memberImage);
    }
}
