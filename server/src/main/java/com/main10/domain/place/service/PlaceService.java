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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.main10.domain.reserve.entity.Reserve.ReserveStatus.RESERVATION_CANCELED;


@Service
@RequiredArgsConstructor
public class PlaceService {

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
     * @param memberId 사용자 식별자
     * @param files 이미지 파일
     * @author LimJaeminZ
     */
    @Transactional
    public void createPlaceS3(PlaceDto.Create placePostDto, Long memberId, List<MultipartFile> files) {

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

        List<String> categoryList = placePostDto.getCategoryList().stream().distinct().collect(Collectors.toList());
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * 공간 저장 (Local)
     *
     * @param placePostDto 공간 등록 dto
     * @param memberId 사용자 식별자
     * @param files 이미지 파일
     * @throws Exception 예외
     * @author LimJaeminZ
     */
    @Transactional
    public void createPlace(PlaceDto.Create placePostDto, Long memberId, List<MultipartFile> files) throws Exception {

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
     * @param memberId 사용자 식별자
     * @return PlaceResponseDto
     * @author LimJaeminZ
     */
    @Transactional
    public PlaceDto.DetailResponse searchPlace(Long placeId, Long memberId) {//, List<String> filePath, List<String> categoryList) {
        boolean isBookmark = false;
        if (memberId != null) {
            Optional<Bookmark> findBookmark = bookmarkRepository
                    .findBookmarkByMemberIdAndPlaceId(memberId, placeId);
            if (findBookmark.isPresent())
                isBookmark = true;
        }

        Place place = placeDbService.ifExistsReturnPlace(placeId);
        Member findMember = memberDbService.ifExistsReturnMember(place.getMemberId());


        List<PlaceImageDto.Response> placeImageResponseDtoList = placeImageService.findAllByPlaceImagePath(placeId);
        List<String> categoryList = placeCategoryService.findByAllPlaceCategoryList(placeId);
        List<String> filePath = new ArrayList<>();
        List<ReserveDto.Detail> reserves = reserveDbService.findAllReserveForPlace(placeId);

        for (PlaceImageDto.Response placeImageResponseDto : placeImageResponseDtoList)
            filePath.add(placeImageResponseDto.getFilePath());

        return new PlaceDto.DetailResponse(place, filePath, categoryList, findMember, isBookmark, reserves);
    }

    /**
     * 공간 수정 (S3)
     *
     * @param placeId 공간 식별자
     * @param placePatchDto 공간 수정 dto
     * @param memberId 사용자 식별자
     * @param files 이미지 파일
     * @author LimjaeminZ
     */
    public void updatePlaceS3(Long placeId, PlaceDto.Update placePatchDto, Long memberId, List<MultipartFile> files) {

        Place updatePlace = placeDbService.ifExistsReturnPlace(placeId);

        if (!Objects.equals(updatePlace.getMemberId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
        }

        updatePlace.setTitle(placePatchDto.getTitle());
        updatePlace.setDetailInfo(placePatchDto.getDetailInfo());
        updatePlace.setAddress(placePatchDto.getAddress());
        updatePlace.setCharge(placePatchDto.getCharge());
        updatePlace.setMaxCapacity(placePatchDto.getMaxCapacity());
        updatePlace.setEndTime(placePatchDto.getEndTime());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db 저장 카테고리 목록
        List<String> categoryList = placePatchDto.getCategoryList(); // 전달되어온 카테고리 목록
        List<String> addCategoryList; // 새롭게 전달되어 온 카테고리를 저장할 List

        // 공간 카테고리 수정
        addCategoryList = placeCategoryService.getAddCategoryList(updatePlace.getId(), dbCategoryList, categoryList);
        addCategoryList = addCategoryList.stream().distinct().collect(Collectors.toList());

        List<PlaceImage> dbPlaceImageList = placeImageService.findAllByPlaceImage(placeId); // db 저장 파일 목록
        List<MultipartFile> addFileList; // 새롭게 전달되어온 파일들의 목록을 저장할 List

        // 파일 업로드 수정
        String dir = "placeImage";
        addFileList = getAddFileList(dbPlaceImageList, files, dir);

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
     * @param memberId 사용자 식별자
     * @param files 이미지 파일
     * @throws Exception 예외
     * @author LimjaeminZ
     */
    public void updatePlace(Long placeId, PlaceDto.Update placePatchDto, Long memberId, List<MultipartFile> files) throws Exception {

        Place updatePlace = placeDbService.ifExistsReturnPlace(placeId);

        if (!Objects.equals(updatePlace.getMemberId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_FOR_UPDATE);
        }
        updatePlace.setTitle(placePatchDto.getTitle());
        updatePlace.setDetailInfo(placePatchDto.getDetailInfo());
        updatePlace.setAddress(placePatchDto.getAddress());
        updatePlace.setCharge(placePatchDto.getCharge());
        updatePlace.setMaxCapacity(placePatchDto.getMaxCapacity());
        updatePlace.setEndTime(placePatchDto.getEndTime());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db 저장 카테고리 목록
        List<String> categoryList = placePatchDto.getCategoryList(); // 전달되어온 카테고리 목록
        List<String> addCategoryList; // 새롭게 전달되어 온 카테고리를 저장할 List

        // 공간 카테고리 수정
        addCategoryList = placeCategoryService.getAddCategoryList(updatePlace.getId(), dbCategoryList, categoryList);

        List<PlaceImage> dbPlaceImageList = placeImageService.findAllByPlaceImage(placeId); // db 저장 파일 목록
        List<MultipartFile> addFileList; // 새롭게 전달되어온 파일들의 목록을 저장할 List

        // 파일 업로드 수정
        String dir = "Local";
        addFileList = getAddFileList(dbPlaceImageList, files, dir);

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
     * @param memberId 사용자 식별자
     * @param placeId 공간 식별자
     * @author Quartz614
     */
    @Transactional
    public void deleteHosting(Long memberId, Long placeId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);
        List<Reserve> findReserve = reserveDbService.findAllByReserves(placeId);

        // 리뷰 삭제
        reviewDbService.deleteAllByReviews(placeId);

        // 북마크 삭제
        bookmarkRepository.deleteAllByPlaceId(placeId);

        // 예약 취소 후 상태 변경
        for (Reserve reserve : findReserve) {
            if (reserve.getStatus().equals(RESERVATION_CANCELED)) {
                throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
            }

            // mbtiCount -1
            mbtiCountService.reduceMbtiCount(findMember, placeId);

            // 예약 상태 변경 ... -> RESERVATION_CANCELED
            reserve.setStatus(RESERVATION_CANCELED);
            reserveDbService.saveReserve(reserve);
        }

        // 공간 카테고리 & 공간 삭제
        placeDbService.deletePlaceCategory(placeId);
        placeDbService.deletePlace(findPlace);
    }

    /**
     * 새롭게 전달받은 파일 리스트 반환 메서드
     *
     * @param dbPlaceImageList 조회된 이미지 리스트
     * @param multipartFileList 새로운 이미지 리스트
     * @param dir Local/S3
     * @return List<MultipartFile>
     * @author LimJaeminZ
     */
    private List<MultipartFile> getAddFileList(List<PlaceImage> dbPlaceImageList, List<MultipartFile> multipartFileList, String dir) {
        List<MultipartFile> addFileList = new ArrayList<>(); // 새롭게 전달되어온 파일들의 목록을 저장할 List

        if (CollectionUtils.isEmpty(dbPlaceImageList)) { // db 존재 x
            if (!CollectionUtils.isEmpty(multipartFileList)) { // 전달된 파일 한장 이상
                // 저장 파일 목록에 추가
                addFileList.addAll(multipartFileList);
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
                List<String> multipartFileOriginNameList = new ArrayList<>();
                multipartFileList.forEach(
                        multipartFile -> multipartFileOriginNameList.add(multipartFile.getOriginalFilename())
                );
                for (PlaceImage placeImage : dbPlaceImageList) {

                    String dbOriginFileName = placeImage.getOriginFileName();

                    if (!multipartFileOriginNameList.contains(dbOriginFileName)) {
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



