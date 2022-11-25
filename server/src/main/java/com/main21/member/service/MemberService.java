package com.main21.member.service;

import com.main21.file.FileHandler;
import com.main21.file.S3Upload;
import com.main21.file.UploadFile;
import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.entity.MemberImage;
import com.main21.security.utils.CustomAuthorityUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final S3Upload s3Upload;
    private final RedisUtils redisUtils;
    private final FileHandler fileHandler;
    private final MemberDbService memberDbService;
    private final CustomAuthorityUtils authorityUtils;


    /**
     * 회원가입 메서드
     * @param post 회원가입 정보
     * @author Quartz614
     */
    public void createMember(MemberDto.Post post) {
        memberDbService.verifyEmail(post);
        List<String> roles = authorityUtils.createRoles(post.getEmail());
        memberDbService.isExistNickname(post.getNickname());

        Member member = Member.builder()
                .email(post.getEmail())
                .password(memberDbService.encodingPassword(post.getPassword()))
                .nickname(post.getNickname())
                .phoneNumber(post.getPhoneNumber())
                .roles(roles)
                .mbti(post.getMbti())
                .build();

        memberDbService.saveMember(member);
    }


    /**
     * 회원정보 수정 메서드
     * @param refreshToken 리프레시 토큰
     * @param patch 회원 수정 정보
     * @author Quartz614
     */
    public void updateMember(String refreshToken, MemberDto.Patch patch) {
        Long memberId = redisUtils.getId(refreshToken);
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        memberDbService.isExistNickname(patch.getNickname());
        findMember.editMember(patch.getNickname(), patch.getMbti());
        memberDbService.saveMember(findMember);
    }


    /**
     * 회원정보 조회 메서드
     * @param refreshToken 리프레시 토큰
     * @return MemberDto.Info
     * @author Quartz614
     */
    public MemberDto.Info getMember(String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);
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
     * @param refreshToken 리프레시 토큰
     * @param multipartFiles 사진 정보
     * @author LimJaeminZ
     */
    @Deprecated
    @SneakyThrows
    public void createProfile(String refreshToken,
                              List<MultipartFile> multipartFiles) {
        Long memberId = redisUtils.getId(refreshToken);
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
     * @param refreshToken 리프레시 토큰
     * @param file 사진 정보
     * @author LimJaeMinZ
     */
    @SneakyThrows
    public void createProfileS3(String refreshToken,
                                MultipartFile file) {
        Long memberId = redisUtils.getId(refreshToken);
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

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
