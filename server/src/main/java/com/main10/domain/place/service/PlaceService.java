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
     * ?????? ?????? (S3)
     *
     * @param placePostDto ?????? ?????? dto
     * @param refreshToken ???????????? ??????
     * @param files ????????? ??????
     * @author LimJaeminZ
     */
    @Transactional
    public void createPlaceS3(PlaceDto.Create placePostDto, String refreshToken, List<MultipartFile> files) {
        //?????? ??????
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

        //????????? ????????? ??? ??????
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //?????? DB ??????
                place.addPlaceImage(placeImage);
            }
        }
        placeDbService.savePlace(place);

        List<String> categoryList = placePostDto.getCategoryList();
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * ?????? ?????? (Local)
     *
     * @param placePostDto ?????? ?????? dto
     * @param refreshToken ???????????? ??????
     * @param files ????????? ??????
     * @throws Exception
     * @author LimJaeminZ
     */
    @Transactional
    public void createPlace(PlaceDto.Create placePostDto, String refreshToken, List<MultipartFile> files) throws Exception {
        //?????? ??????
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

        //????????? ????????? ??? ??????
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //?????? DB ??????
                place.addPlaceImage(placeImage);
            }
        }
        placeDbService.savePlace(place);

        List<String> categoryList = placePostDto.getCategoryList();
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * ?????? ?????? ?????? ?????????
     *
     * @param placeId ?????? ?????????
     * @param refreshToken ???????????? ??????
     * @return PlaceResponseDto
     * @author LimJaeminZ
     */
    @Transactional
    public PlaceDto.DetailResponse searchPlace(Long placeId, String refreshToken) {//, List<String> filePath, List<String> categoryList) {
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


        List<PlaceImageDto.Response> placeImageResponseDtoList = placeImageService.findAllByPlaceImagePath(placeId);
        List<String> categoryList = placeCategoryService.findByAllPlaceCategoryList(placeId);
        List<String> filePath = new ArrayList<>();
        List<ReserveDto.Detail> reserves = reserveDbService.findAllReserveForPlace(placeId);

        for (PlaceImageDto.Response placeImageResponseDto : placeImageResponseDtoList)
            filePath.add(placeImageResponseDto.getFilePath());

        return new PlaceDto.DetailResponse(place, filePath, categoryList, findMember, isBookmark, reserves);
    }

    /**
     * ?????? ?????? (S3)
     *
     * @param placeId ?????? ?????????
     * @param placePatchDto ?????? ?????? dto
     * @param refreshToken ???????????? ??????
     * @param files ????????? ??????
     * @author LimjaeminZ
     */
    public void updatePlaceS3(Long placeId, PlaceDto.Update placePatchDto, String refreshToken, List<MultipartFile> files) {
        //?????? ??????
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
        updatePlace.setEndTime(placePatchDto.getEndTime());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db ?????? ???????????? ??????
        List<String> categoryList = placePatchDto.getCategoryList(); // ??????????????? ???????????? ??????
        List<String> addCategoryList; // ????????? ???????????? ??? ??????????????? ????????? List

        // ?????? ???????????? ??????
        addCategoryList = placeCategoryService.getAddCategoryList(updatePlace.getId(), dbCategoryList, categoryList);

        List<PlaceImage> dbPlaceImageList = placeImageService.findAllByPlaceImage(placeId); // db ?????? ?????? ??????
        List<MultipartFile> multipartFileList = files; //placePatchDto.getMultipartFiles(); // ??????????????? ?????? ??????
        List<MultipartFile> addFileList; // ????????? ??????????????? ???????????? ????????? ????????? List

        // ?????? ????????? ??????
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

        //????????? ????????? ??? ??????
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //?????? DB ??????
                updatePlace.addPlaceImage(placeDbService.savePlaceImage(placeImage));
            }
        }
        placeDbService.savePlace(updatePlace);
        placeCategoryService.saveCategoryList(addCategoryList, updatePlace);
    }


    /**
     * ?????? ?????? (Local)
     *
     * @param placeId ?????? ?????????
     * @param placePatchDto ?????? ?????? dto
     * @param refreshToken ???????????? ??????
     * @param files ????????? ??????
     * @throws Exception
     * @author LimjaeminZ
     */
    public void updatePlace(Long placeId, PlaceDto.Update placePatchDto, String refreshToken, List<MultipartFile> files) throws Exception {
        //?????? ??????
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
        updatePlace.setEndTime(placePatchDto.getEndTime());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db ?????? ???????????? ??????
        List<String> categoryList = placePatchDto.getCategoryList(); // ??????????????? ???????????? ??????
        List<String> addCategoryList; // ????????? ???????????? ??? ??????????????? ????????? List

        // ?????? ???????????? ??????
        addCategoryList = placeCategoryService.getAddCategoryList(updatePlace.getId(), dbCategoryList, categoryList);

        List<PlaceImage> dbPlaceImageList = placeImageService.findAllByPlaceImage(placeId); // db ?????? ?????? ??????
        List<MultipartFile> multipartFileList = files; //placePatchDto.getMultipartFiles(); // ??????????????? ?????? ??????
        List<MultipartFile> addFileList; // ????????? ??????????????? ???????????? ????????? ????????? List

        // ?????? ????????? ??????
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

        //????????? ????????? ??? ??????
        if (!placeImageList.isEmpty()) {
            for (PlaceImage placeImage : placeImageList) {
                //?????? DB ??????
                updatePlace.addPlaceImage(placeImage);
            }
        }
        placeDbService.savePlace(updatePlace);
        placeCategoryService.saveCategoryList(addCategoryList, updatePlace);

        // ?????? ?????? ?????? maxSpace update
        // timeStatusService.updateIsFull(updatePlace);
    }

    /**
     * ?????? ?????? ?????????
     *
     * @param refreshToken ???????????? ??????
     * @param placeId ?????? ?????????
     * @author Quartz614
     */
    @Transactional
    public void deleteHosting(String refreshToken, Long placeId) {
        Long memberId = redisUtils.getId(refreshToken);
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);
        List<Reserve> findReserve = reserveDbService.findAllByReserves(placeId);

        // ?????? ??????
        reviewDbService.deleteAllByReviews(placeId);

        // ????????? ??????
        bookmarkRepository.deleteAllByPlaceId(placeId);

        // ?????? ?????? ??? ?????? ??????
        for (int i = 0; i < findReserve.size(); i++) {
            if (findReserve.get(i).getStatus().equals(RESERVATION_CANCELED)) {
                throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
            }

            // mbtiCount -1
            mbtiCountService.reduceMbtiCount(findMember, placeId);

            // ?????? ?????? ?????? ... -> RESERVATION_CANCELED
            findReserve.get(i).setStatus(RESERVATION_CANCELED);
            reserveDbService.saveReserve(findReserve.get(i));
        }

        // ?????? ???????????? & ?????? ??????
        placeDbService.deletePlaceCategory(placeId);
        placeDbService.deletePlace(findPlace);
    }

    /**
     * ????????? ???????????? ?????? ????????? ?????? ?????????
     *
     * @param dbPlaceImageList ????????? ????????? ?????????
     * @param multipartFileList ????????? ????????? ?????????
     * @param dir Local/S3
     * @return List<MultipartFile>
     * @author LimJaeminZ
     */
    private List<MultipartFile> getAddFileList(List<PlaceImage> dbPlaceImageList, List<MultipartFile> multipartFileList, String dir) {
        List<MultipartFile> addFileList = new ArrayList<>(); // ????????? ??????????????? ???????????? ????????? ????????? List

        if (CollectionUtils.isEmpty(dbPlaceImageList)) { // db ?????? x
            if (!CollectionUtils.isEmpty(multipartFileList)) { // ????????? ?????? ?????? ??????
                multipartFileList.forEach(
                        multipartFile -> {
                            addFileList.add(multipartFile); // ?????? ?????? ????????? ??????
                        }
                );
            }
        } else { // db??? ?????? ?????? ??????
            if (CollectionUtils.isEmpty(multipartFileList)) { // ????????? ?????? x
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
            } else { // ????????? ?????? ?????? ??????
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



