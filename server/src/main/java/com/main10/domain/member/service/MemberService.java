package com.main10.domain.member.service;

import com.main10.domain.member.entity.AccountStatus;
import com.main10.domain.member.repository.MemberImageRepository;
import com.main10.global.file.FileHandler;
import com.main10.global.file.S3Upload;
import com.main10.global.file.UploadFile;
import com.main10.domain.member.dto.MemberDto;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.entity.MemberImage;
import com.main10.global.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final S3Upload s3Upload;
    private final FileHandler fileHandler;
    private final MemberDbService memberDbService;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberImageRepository memberImageRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    /**
     * 회원가입 메서드
     * @param post 회원가입 정보
     * @author Quartz614
     */
    public void createMember(MemberDto.Post post) {
        memberDbService.verifyEmail(post);
        memberDbService.isExistNickname(post.getNickname());
        String provider = "common";
        List<String> roles = authorityUtils.createRoles(post.getEmail(), provider);

        Member member = Member.builder()
                .email(post.getEmail())
                .password(memberDbService.encodingPassword(post.getPassword()))
                .nickname(post.getNickname())
                .phoneNumber(post.getPhoneNumber())
                .accountStatus(AccountStatus.COMMON_MEMBER)
                .roles(roles)
                .mbti(post.getMbti())
                .build();

        // Local default Image
/*        MemberImage memberImageLocal = MemberImage.builder()
                .filePath(defaultImageAddress)
                .build();
        member.addMemberImage(memberImageLocal);*/

        // S3 default Image
        MemberImage memberImageS3 = MemberImage.builder()
                .filePath(defaultImageAddress)
                .build();
        member.addMemberImage(memberImageS3);

        memberDbService.saveMember(member);
    }

    /**
     * 회원정보 수정 메서드
     * @param memberId 사용자 식별자
     * @param patch 회원 수정 정보
     * @author Quartz614
     */
    public void updateMember(Long memberId, MemberDto.Patch patch) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        memberDbService.isExistNickname(patch.getNickname());
        findMember.editMember(patch.getNickname(), patch.getMbti());
        memberDbService.saveMember(findMember);
    }

    /**
     * 회원정보 조회 메서드
     * @param memberId 사용자 식별자
     * @return MemberDto.Info
     * @author Quartz614
     */
    public MemberDto.Info getMember(Long memberId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        if (findMember.getMemberImage() != null) {
            return MemberDto.Info.builder()
                    .profileImage(findMember.getMemberImage().getFilePath())
                    .nickname(findMember.getNickname())
                    .mbti(findMember.getMbti())
                    .build();
        } else {
            return MemberDto.Info.builder()
                    .nickname(findMember.getNickname())
                    .mbti(findMember.getMbti())
                    .build();
        }
    }

    /**
     * 프로필 사진 업로드 메서드(Deprecated)
     * @param memberId 사용자 식별자
     * @param multipartFiles 사진 정보
     * @author LimJaeminZ
     */
    @Deprecated
    @SneakyThrows
    public void createProfile(Long memberId,
                              List<MultipartFile> multipartFiles) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

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

        if (!memberImageList.isEmpty()) {
            for (MemberImage memberImage : memberImageList) {
                //유저 FK 저장 관계
                findMember.addMemberImage(memberImage);
                //파일 DB 저장
                memberDbService.saveMember(findMember);
                break;
            }
        }
    }

    /**
     * 회원 프로필 사진 업로드 S3 메서드
     * @param memberId 사용자 식별자
     * @param file 사진 정보
     * @author LimJaeMinZ
     */
    @SneakyThrows
    @Transactional
    public void createProfileS3(Long memberId,
                                MultipartFile file) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        if(!file.isEmpty()) {
            memberImageRepository.deleteByMemberId(memberId);
        }

        String dir = "memberImage";
        UploadFile uploadFile = s3Upload.uploadfile(file, dir);

        MemberImage memberImage = MemberImage.builder()
                .originFileName(uploadFile.getOriginFileName())
                .fileName(uploadFile.getFileName())
                .filePath(uploadFile.getFilePath())
                .fileSize(uploadFile.getFileSize())
                .build();

        findMember.addMemberImage(memberImage);
        memberDbService.saveMember(findMember);
    }
}
