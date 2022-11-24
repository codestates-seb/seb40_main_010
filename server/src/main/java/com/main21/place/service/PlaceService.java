package com.main21.place.service;

import com.main21.bookmark.entity.Bookmark;
import com.main21.bookmark.repository.BookmarkRepository;
import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.file.FileHandler;
import com.main21.file.S3Upload;
import com.main21.file.UploadFile;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.place.dto.*;

import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import com.main21.place.repository.PlaceCategoryRepository;
import com.main21.place.repository.PlaceImageRepository;
import com.main21.place.repository.PlaceRepository;
import com.main21.reserve.entity.*;
import com.main21.reserve.repository.HostingTimeRepository;
import com.main21.reserve.repository.MbtiCountRepository;
import com.main21.reserve.repository.ReserveRepository;
import com.main21.review.repository.ReviewRepository;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final FileHandler fileHandler;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final RedisUtils redisUtils;
    private final ReviewRepository reviewRepository;
    private final ReserveRepository reserveRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final MbtiCountRepository mbtiCountRepository;
    private final HostingTimeRepository hostingTimeRepository;
    private final PlaceImageService placeImageService;
    private final PlaceCategoryService placeCategoryService;
    //private final TimeStatusService timeStatusService;

    @Autowired
    private S3Upload s3Upload;

    /**
     * 장소 저장 (S3)
     * @param placePostDto
     * @param refreshToken
     * @param files
     */
    @Transactional
    public void createPlaceS3(PlacePostDto placePostDto, String refreshToken, List<MultipartFile> files) {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place place = Place.builder()
                .title(placePostDto.getTitle())
                .detailInfo(placePostDto.getDetailInfo())
                .maxCapacity(placePostDto.getMaxCapacity())
                .maxSpace(placePostDto.getMaxSpace())
                .address(placePostDto.getAddress())
                .charge(placePostDto.getCharge())
                .memberId(memberId)
                .score(placePostDto.getScore())
                .view(placePostDto.getView())
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
        placeRepository.save(place);

        List<String> categoryList = placePostDto.getCategoryList();
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * 장소 저장 (Local)
     * @param placePostDto
     * @param refreshToken
     * @param files
     * @throws Exception
     */
    @Transactional
    public void createPlace(PlacePostDto placePostDto, String refreshToken, List<MultipartFile> files) throws Exception {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place place = Place.builder()
                .title(placePostDto.getTitle())
                .detailInfo(placePostDto.getDetailInfo())
                .maxCapacity(placePostDto.getMaxCapacity())
                .maxSpace(placePostDto.getMaxSpace())
                .address(placePostDto.getAddress())
                .charge(placePostDto.getCharge())
                .memberId(memberId)
                .score(placePostDto.getScore())
                .view(placePostDto.getView())
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
        placeRepository.save(place);

        List<String> categoryList = placePostDto.getCategoryList();
        placeCategoryService.saveCategoryList(categoryList, place);
    }

    /**
     * 장소 상세 조회 메서드
     * @param placeId
     * @param refreshToken
     * @return
     */
    @Transactional
    public PlaceResponseDto searchPlace(Long placeId, String refreshToken) {//, List<String> filePath, List<String> categoryList) {

        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));
        Optional<Bookmark> findBookmark = bookmarkRepository.findBookmarkByMemberIdAndPlaceId(place.getMemberId(), placeId);
        Optional<Member> findMember = memberRepository.findById(place.getMemberId());

        List<PlaceImageResponseDto> placeImageResponseDtoList = placeImageService.findAllByPlaceImagePath(placeId);
        List<String> categoryList = placeCategoryService.findByAllPlaceCategoryList(placeId);
        List<String> filePath = new ArrayList<>();

        for (PlaceImageResponseDto placeImageResponseDto : placeImageResponseDtoList)
            filePath.add(placeImageResponseDto.getFilePath());

        boolean isBookmark = false;

        // if -> refreshToken이 존재하지 않으면 무조건 false 반환,
        // else -> refreshToken와 bookmark의 memberId가 같고 bookmark가 존재하면 true 반환
        if (refreshToken == null) {
            isBookmark = false;
        }else {
            Long memberId = redisUtils.getId(refreshToken);
            if (findBookmark.isPresent() && memberId.equals(findBookmark.get().getMemberId())) {
                isBookmark = true;
            }
        }

        return new PlaceResponseDto(place, filePath, categoryList, findMember.get(), isBookmark);
    }

    /**
     * 장소 수정 (S3)
     * @param placeId
     * @param placePatchDto
     * @param refreshToken
     * @param files
     */
    public void updatePlaceS3(Long placeId, PlacePatchDto placePatchDto, String refreshToken, List<MultipartFile> files) {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place updatePlace = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        if (!Objects.equals(updatePlace.getMemberId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_UPDATE);
        }

        updatePlace.setTitle(placePatchDto.getTitle());
        updatePlace.setDetailInfo(placePatchDto.getDetailInfo());
        updatePlace.setAddress(placePatchDto.getAddress());
        updatePlace.setCharge(placePatchDto.getCharge());
        updatePlace.setMaxCapacity(placePatchDto.getMaxCapacity());
        updatePlace.setMaxSpace(placePatchDto.getMaxSpace());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db 저장 카테고리 목록
        List<String> categoryList = placePatchDto.getCategoryList(); // 전달되어온 카테고리 목록
        List<String> addCategoryList; // 새롭게 전달되어 온 카테고리를 저장할 List

        // 장소카테고리 수정
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
                updatePlace.addPlaceImage(placeImageRepository.save(placeImage));
            }
        }
        placeRepository.save(updatePlace);
        placeCategoryService.saveCategoryList(addCategoryList, updatePlace);
    }


    /**
     * 장소 수정 (Local)
     * @param placeId
     * @param placePatchDto
     * @param refreshToken
     * @param files
     * @throws Exception
     */
    public void updatePlace(Long placeId, PlacePatchDto placePatchDto, String refreshToken, List<MultipartFile> files) throws Exception {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);

        Place updatePlace = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        if (!Objects.equals(updatePlace.getMemberId(), memberId)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_UPDATE);
        }
        updatePlace.setTitle(placePatchDto.getTitle());
        updatePlace.setDetailInfo(placePatchDto.getDetailInfo());
        updatePlace.setAddress(placePatchDto.getAddress());
        updatePlace.setCharge(placePatchDto.getCharge());
        updatePlace.setMaxCapacity(placePatchDto.getMaxCapacity());
        updatePlace.setMaxSpace(placePatchDto.getMaxSpace());

        List<PlaceCategory> dbCategoryList = placeCategoryService.findByAllPlaceCategoryList2(placeId); // db 저장 카테고리 목록
        List<String> categoryList = placePatchDto.getCategoryList(); // 전달되어온 카테고리 목록
        List<String> addCategoryList; // 새롭게 전달되어 온 카테고리를 저장할 List

        // 장소카테고리 수정
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
        placeRepository.save(updatePlace);
        placeCategoryService.saveCategoryList(addCategoryList, updatePlace);

        // 수정 날짜 이후 maxSpace update
        // timeStatusService.updateIsFull(updatePlace);
    }


    /**
     * Pagination 적용한 공간 전체 조회 메서드
     *
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
    public Page<PlaceDto.Response> getPlacesPage(Pageable pageable) {
        return placeRepository.getPlacesPage(pageable);
    }


    /**
     * Slice 무한스크롤 공간 전체 조회 메서드
     * @param pageable
     * @return
     @author LeeGoh
     */
    /*
    @Transactional
    public Slice<PlaceDto.Response> getPlacesSlice(Pageable pageable) {
        return placeRepository.getPlacesSlice(pageable);
    }
     */

    /**
     * Pagination 적용한 카테고리별 공간 조회 메서드
     *
     * @param categoryId
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
    public Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable) {
        return placeRepository.getCategoryPage(categoryId, pageable);
    }

    /**
     * Slice 무한스크롤 카테고리별 공간 조회 메서드
     * @param categoryId
     * @param pageable
     * @return
     @author LeeGoh
     */
    /*
    @Transactional
    public Slice<PlaceCategoryDto.Response> getCategorySlice(Long categoryId, Pageable pageable) {
        return placeRepository.getCategorySlice(categoryId, pageable);
    }
     */


    /**
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색 메서드
     *
     * @param searchDetail
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
    public Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        return placeRepository.searchDetail(searchDetail, pageable);
    }

    /**
     * 공간 전체 타이틀 검색 메서드
     *
     * @param title
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
    public Page<PlaceDto.Response> searchTitleAll(String title, Pageable pageable) {
        return placeRepository.searchTitle(split(title), pageable);
    }

    /**
     * 공간 카테고리별 타이틀 검색 메서드
     *
     * @param categoryId
     * @param title
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Transactional
    public Page<PlaceCategoryDto.Response> searchTitleCategory(Long categoryId, String title, Pageable pageable) {
        return placeRepository.searchCategoryTitle(categoryId, split(title), pageable);
    }

    /**
     * Stirng 검색어 공백을 기준으로 분리하여 List에 담는 메서드
     *
     * @param title
     * @return
     * @author LeeGoh
     */
    public List<String> split(String title) {
        List<String> titles = new ArrayList<>();
        String[] list = title.split(" ");
        for (int i = 0; i < list.length; i++) {
            titles.add(list[i]);
        }
        return titles;
    }

    /**
     * getPlaceMypage
     *
     * @param pageable
     * @return
     * @author quartz614
     */
    @Transactional
    public Page<PlaceDto.Response> getPlaceMypage(String refreshToken, Pageable pageable) {
        //유저 확인
        Long memberId = redisUtils.getId(refreshToken);
        return placeRepository.getPlaceMypage(memberId, pageable);
    }

    @Transactional
    public void deleteHosting(String refreshToken, Long placeId) {
        Long memberId = redisUtils.getId(refreshToken);
        Member findMember = memberRepository
                .findById(memberId)
                .orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Place findPlace = placeRepository
                .findById(placeId)
                .orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));
        List<Reserve> findReserve = reserveRepository
                .findAllByPlaceId(placeId);

        // 리뷰 삭제
        reviewRepository.deleteAllByPlaceId(placeId);

        // 북마크 삭제
        bookmarkRepository.deleteAllByPlaceId(placeId);

        // 호스팅 타임 삭제
        hostingTimeRepository.deleteAllByPlaceId(placeId);

        // 예약 취소 후 상태 변경
        for (int i = 0; i < findReserve.size(); i++) {
            if (findReserve.get(i).getStatus().equals(Reserve.ReserveStatus.RESERVATION_CANCELED)) {
                throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
            }

            // mbtiCount -1
            MbtiCount findMBTICount = mbtiCountRepository.findMbtiCountByMbtiAndPlaceId(findMember.getMbti(), placeId)
                    .orElse(null);
            findMBTICount.reduceOneMbti();
            mbtiCountRepository.save(findMBTICount);

            // 예약 상태 변경 ... -> RESERVATION_CANCELED
            findReserve.get(i).setStatus(Reserve.ReserveStatus.RESERVATION_CANCELED);
            reserveRepository.save(findReserve.get(i));
        }

        // 장소 카테고리 & 호스팅 삭제
        placeCategoryRepository.deleteAllByPlaceId(placeId);
        placeRepository.delete(findPlace);
    }

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



