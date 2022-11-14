package com.main21.place.service;

import com.main21.place.dto.PlacePostDto;
import com.main21.place.entity.Category;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import com.main21.place.repository.CategoryRepository;
import com.main21.place.repository.PlaceCategoryRepository;
import com.main21.place.repository.PlaceImageRepository;
import com.main21.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceImageRepository placeImageRepository;
    private final FileHandler fileHandler;

    private final CategoryRepository categoryRepository;
    private final PlaceCategoryRepository placeCategoryRepository;

    /*
     * 공간 등록 메서드
     * @param place
     * @return
     */
    @Transactional
    public Long create(PlacePostDto postDto, List<MultipartFile> files) throws Exception {
        //유저 확인 필요

        Place place = new Place(
                postDto.getTitle(),
                postDto.getDetailInfo(),
                postDto.getMaxCapacity(),
                postDto.getAddress(),
                postDto.getCharge()
        );

        List<PlaceImage> placeImageList = fileHandler.parseFileInfo(files);

        //파일이 존재할 때 처리
        if(!placeImageList.isEmpty()) {
            for(PlaceImage placeImage : placeImageList) {
                //파일 DB 저장
                place.addPlaceImage(placeImageRepository.save(placeImage));
            }
        }

        Long placeId = placeRepository.save(place).getId();

        List<String> categoryList = postDto.getCategoryList();
        categoryList.forEach(
                s -> {
                    Category category = categoryRepository.findByCategoryName(s);
                    PlaceCategory placeCategory = new PlaceCategory(place, s, category);
                    placeCategoryRepository.save(placeCategory);
                }
        );

        return placeId;
    }
}
