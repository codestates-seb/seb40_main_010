package com.main10.domain.place.service;

import com.main10.domain.bookmark.entity.Bookmark;
import com.main10.domain.bookmark.repository.BookmarkRepository;
import com.main10.domain.place.dto.*;
import com.main10.domain.place.entity.PlaceCategory;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.global.file.FileHandler;
import com.main10.global.file.S3Upload;
import com.main10.global.file.UploadFile;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.service.MemberDbService;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceImage;
import com.main10.domain.reserve.dto.ReserveDto;
import com.main10.domain.reserve.entity.Reserve;
import com.main10.global.batch.service.MbtiCountService;
import com.main10.domain.reserve.service.ReserveDbService;
import com.main10.domain.review.service.ReviewDbService;
import com.main10.global.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.main10.domain.reserve.entity.Reserve.ReserveStatus.RESERVATION_CANCELED;


@Service
@RequiredArgsConstructor
public class PlaceService {

    private final RedisUtils redisUtils;
    private final PlaceDbService placeDbService;
    private final ReserveDbService reserveDbService;
    private final MemberDbService memberDbService;
    private final ReviewDbService reviewDbService;

    private final FileHandler fileHandler;

    private final MbtiCountService mbtiCountService;
    private final PlaceImageService placeImageService;
    private final PlaceCategoryService placeCategoryService;
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    private S3Upload s3Upload;

    /**
     * 공간 저장 (S3)
     *
     * @param placePostDto 공간 등록 dto
     * @param refreshToken 리프래시 토큰
     * @param files 이미지 파일
     * @author LimJaeminZ
     */
    @Transactional
    public void createPlaceS3(PlacePostDto placePostDto, String refreshToken, List<MultipartFile> files) {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place place = Place.builder()
                .title(placePostDto.getTitle())
                .detailInfo(placePostDto.getDetailInfo())
                .maxCapacity(placePostDto.getMaxCapacity())
                .address(placePostDto.getAddress())
                .charge(placePostDto.getCharge())
                .memberId(memberId)
                .score(placePostDto.getScore())
                .endTime(placePostDto.getEndTime())
                .build();

        String dir = "placeImage";
        List<UploadFile> uploadFileList = s3Upload.uploadFileList(files, dir);
        List<PlaceImage> placeImageList = new ArrayList<>();

        uploadFileList.forEach(uploadFile -> {
            PlaceImage placeImage = PlaceImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();
            placeImageList.add(placeImage);
        });

        //파일이 존재할 때 처리
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImage);
            }
        }
        placeDbService.savePlace(place);

        List<String> categoryList = placePostDto.getCategoryList();
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * 공간 저장 (Local)
     *
     * @param placePostDto 공간 등록 dto
     * @param refreshToken 리프래시 토큰
     * @param files 이미지 파일
     * @throws Exception
     * @author LimJaeminZ
     */
    @Transactional
    public void createPlace(PlacePostDto placePostDto, String refreshToken, List<MultipartFile> files) throws Exception {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place place = Place.builder()
                .title(placePostDto.getTitle())
                .detailInfo(placePostDto.getDetailInfo())
                .maxCapacity(placePostDto.getMaxCapacity())
                .address(placePostDto.getAddress())
                .charge(placePostDto.getCharge())
                .memberId(memberId)
                .score(placePostDto.getScore())
                .endTime(placePostDto.getEndTime())
                .build();

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(files);
        List<PlaceImage> placeImageList = new ArrayList<>();

        uploadFileList.forEach(uploadFile -> {
            PlaceImage placeImage = PlaceImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();
            placeImageList.add(placeImage);
        });

        //파일이 존재할 때 처리
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImage);
            }
        }
        placeDbService.savePlace(place);

        List<String> categoryList = placePostDto.getCategoryList();
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * 공간 상세 조회 메서드
     *
     * @param placeId 공간 식별자
     * @param refreshToken 리프래시 토큰
     * @return PlaceResponseDto
     * @author LimJaeminZ
     */
    @Transactional
    public PlaceResponseDto searchPlace(Long placeId, String refreshToken) {//, List<String> filePath, List<String> categoryList) {
        boolean isBookmark = false;

        if (StringUtils.hasText(refreshToken)) {
            Long memberId = redisUtils.getId(refreshToken);
            Optional<Bookmark> findBookmark = bookmarkRepository
                    .findBookmarkByMemberIdAndPlaceId(memberId, placeId);
            if (findBookmark.isPresent())
                isBookmark = true;
        }


        Place place = placeDbService.ifExistsReturnPlace(placeId);
        Member findMember = memberDbService.ifExistsReturnMember(place.getMemberId());


        List<PlaceImageResponseDto> placeImageResponseDtoList = placeImageService.findAllByPlaceImagePath(placeId);
        List<String> categoryList = placeCategoryService.findByAllPlaceCategoryList(placeId);
        List<String> filePath = new ArrayList<>();
        List<ReserveDto.Detail> reserves = reserveDbService.findAllReserveForPlace(placeId);

        for (PlaceImageResponseDto placeImageResponseDto : placeImageResponseDtoList)
            filePath.add(placeImageResponseDto.getFilePath());

        return new PlaceResponseDto(place, filePath, categoryList, findMember, isBookmark, reserves);
    }

    /**
     * 공간 수정 (S3)
     *
     * @param placeId 공간 식별자
     * @param placePatchDto 공간 수정 dto
     * @param refreshToken 리프래시 토큰
     * @param files 이미지 파일
     * @author LimjaeminZ
     */
    public void updatePlaceS3(Long placeId, PlacePatchDto placePatchDto, String refreshToken, List<MultipartFile> files) {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place updatePlace = placeDbService.ifExistsReturnPlace(placeId);

        if (!Objects.equals(updatePlace.getMemberId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
        }

        updatePlace.setTitle(placePatchDto.getTitle());
        updatePlace.setDetailInfo(placePatchDto.getDetailInfo());
        updatePlace.setAddress(placePatchDto.getAddress());
        updatePlace.setCharge(placePatchDto.getCharge());
        updatePlace.setMaxCapacity(placePatchDto.getMaxCapacity());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db 저장 카테고리 목록
        List<String> categoryList = placePatchDto.getCategoryList(); // 전달되어온 카테고리 목록
        List<String> addCategoryList; // 새롭게 전달되어 온 카테고리를 저장할 List

        // 공간 카테고리 수정
        addCategoryList = placeCategoryService.getAddCategoryList(updatePlace.getId(), dbCategoryList, categoryList);

        List<PlaceImage> dbPlaceImageList = placeImageService.findAllByPlaceImage(placeId); // db 저장 파일 목록
        List<MultipartFile> multipartFileList = files; //placePatchDto.getMultipartFiles(); // 전달되어온 파일 목록
        List<MultipartFile> addFileList; // 새롭게 전달되어온 파일들의 목록을 저장할 List

        // 파일 업로드 수정
        String dir = "placeImage";
        addFileList = getAddFileList(dbPlaceImageList, multipartFileList, dir);

        List<UploadFile> uploadFileList = s3Upload.uploadFileList(addFileList, dir);
        List<PlaceImage> placeImageList = new ArrayList<>();

        uploadFileList.forEach(uploadFile -> {
            PlaceImage placeImage = PlaceImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();
            placeImageList.add(placeImage);
        });

        //파일이 존재할 때 처리
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                updatePlace.addPlaceImage(placeDbService.savePlaceImage(placeImage));
            }
        }
        placeDbService.savePlace(updatePlace);
        placeCategoryService.saveCategoryList(addCategoryList, updatePlace);
    }


    /**
     * 공간 수정 (Local)
     *
     * @param placeId 공간 식별자
     * @param placePatchDto 공간 수정 dto
     * @param refreshToken 리프래시 토큰
     * @param files 이미지 파일
     * @throws Exception
     * @author LimjaeminZ
     */
    public void updatePlace(Long placeId, PlacePatchDto placePatchDto, String refreshToken, List<MultipartFile> files) throws Exception {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place updatePlace = placeDbService.ifExistsReturnPlace(placeId);

        if (!Objects.equals(updatePlace.getMemberId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
        }
        updatePlace.setTitle(placePatchDto.getTitle());
        updatePlace.setDetailInfo(placePatchDto.getDetailInfo());
        updatePlace.setAddress(placePatchDto.getAddress());
        updatePlace.setCharge(placePatchDto.getCharge());
        updatePlace.setMaxCapacity(placePatchDto.getMaxCapacity());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db 저장 카테고리 목록
        List<String> categoryList = placePatchDto.getCategoryList(); // 전달되어온 카테고리 목록
        List<String> addCategoryList; // 새롭게 전달되어 온 카테고리를 저장할 List

        // 공간 카테고리 수정
        addCategoryList = placeCategoryService.getAddCategoryList(updatePlace.getId(), dbCategoryList, categoryList);

        List<PlaceImage> dbPlaceImageList = placeImageService.findAllByPlaceImage(placeId); // db 저장 파일 목록
        List<MultipartFile> multipartFileList = files; //placePatchDto.getMultipartFiles(); // 전달되어온 파일 목록
        List<MultipartFile> addFileList; // 새롭게 전달되어온 파일들의 목록을 저장할 List

        // 파일 업로드 수정
        String dir = "Local";
        addFileList = getAddFileList(dbPlaceImageList, multipartFileList, dir);

        List<UploadFile> uploadFileList = fileHandler.parseUploadFileInfo(addFileList);
        List<PlaceImage> placeImageList = new ArrayList<>();

        uploadFileList.forEach(uploadFile -> {
            PlaceImage placeImage = PlaceImage.builder()
                    .originFileName(uploadFile.getOriginFileName())
                    .fileName(uploadFile.getFileName())
                    .filePath(uploadFile.getFilePath())
                    .fileSize(uploadFile.getFileSize())
                    .build();
            placeImageList.add(placeImage);
        });

        //파일이 존재할 때 처리
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                updatePlace.addPlaceImage(placeImage);
            }
        }
        placeDbService.savePlace(updatePlace);
        placeCategoryService.saveCategoryList(addCategoryList, updatePlace);

        // 수정 날짜 이후 maxSpace update
        // timeStatusService.updateIsFull(updatePlace);
    }

    /**
     * 공간 삭제 메서드
     *
     * @param refreshToken 리프래시 토큰
     * @param placeId 공간 식별자
     * @author Quartz614
     */
    @Transactional
    public void deleteHosting(String refreshToken, Long placeId) {
        Long memberId = redisUtils.getId(refreshToken);
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);
        List<Reserve> findReserve = reserveDbService.findAllByReserves(placeId);

        // 리뷰 삭제
        reviewDbService.deleteAllByReviews(placeId);

        // 북마크 삭제
        bookmarkRepository.deleteAllByPlaceId(placeId);

        // 예약 취소 후 상태 변경
        for (int i = 0; i < findReserve.size(); i++) {
            if (findReserve.get(i).getStatus().equals(RESERVATION_CANCELED)) {
                throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
            }

            // mbtiCount -1
            mbtiCountService.reduceMbtiCount(findMember, placeId);

            // 예약 상태 변경 ... -> RESERVATION_CANCELED
            findReserve.get(i).setStatus(RESERVATION_CANCELED);
            reserveDbService.saveReserve(findReserve.get(i));
        }

        // 공간 카테고리 & 공간 삭제
        placeDbService.deletePlaceCategory(placeId);
        placeDbService.deletePlace(findPlace);
    }

    /**
     *
     * @param dbPlaceImageList
     * @param multipartFileList
     * @param dir
     * @return List<MultipartFile>
     * @author LimJaeminZ
     */
    private List<MultipartFile> getAddFileList(List<PlaceImage> dbPlaceImageList, List<MultipartFile> multipartFileList, String dir) {
        List<MultipartFile> addFileList = new ArrayList<>(); // 새롭게 전달되어온 파일들의 목록을 저장할 List

        if (CollectionUtils.isEmpty(dbPlaceImageList)) { // db 존재 x
            if (!CollectionUtils.isEmpty(multipartFileList)) { // 전달된 파일 한장 이상
                multipartFileList.forEach(
                        multipartFile -> {
                            addFileList.add(multipartFile); // 저장 파일 목록에 추가
                        }
                );
            }
        } else { // db에 한장 이상 존재
            if (CollectionUtils.isEmpty(multipartFileList)) { // 전달된 파일 x
                dbPlaceImageList.forEach(
                        placeImage -> {
                            if(dir.equals("Local")) {
                                placeImageService.delete(placeImage.getId());
                            }
                            else {
                                placeImageService.delete(placeImage.getId());
                                s3Upload.delete(placeImage.getFileName(), dir);
                            }
                        }
                );
            } else { // 전달된 파일 한장 이상
                List<String> dbOriginFileNameList = new ArrayList<>();

                for (PlaceImage placeImage : dbPlaceImageList) {
                    PlaceImageDto dbPlaceImageDto = placeImageService.findByFiledId(placeImage.getId());
                    String dbOriginFileName = dbPlaceImageDto.getOriginFileName();

                    if (!multipartFileList.contains(dbOriginFileName)) {
                        if(dir.equals("Local")) {
                            placeImageService.delete(placeImage.getId());
                        }
                        else {
                            placeImageService.delete(placeImage.getId());
                            s3Upload.delete(placeImage.getFileName(), dir);
                        }
                    } else {
                        dbOriginFileNameList.add(dbOriginFileName);
                    }
                }
                for (MultipartFile multipartFile : multipartFileList) {
                    String multipartOriginName = multipartFile.getOriginalFilename();
                    if (!dbOriginFileNameList.contains(multipartOriginName)) {
                        addFileList.add(multipartFile);
                    }
                }
            }
        }
        return addFileList;
    }
}



