package com.main21.place.repository;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.dto.QPlaceCategoryDto_Response;
import com.main21.place.dto.QPlaceDto_Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.place.entity.QPlace.place;
import static com.main21.place.entity.QPlaceCategory.placeCategory;

public class PlaceRepositoryImpl implements CustomPlaceRepository{

    private final JPAQueryFactory queryFactory;

    public PlaceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    @Override
//    public List<PlaceDto.Response> getPlaces() {
//        List<PlaceDto.Response> results = queryFactory
//                .select(new QPlaceDto_Response(place))
//                .from(place)
//                .fetch();
//
//        return results;
//    }
    @Override
    public Page<PlaceDto.Response> getPlaces(Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(place))
                .from(place)
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<PlaceCategoryDto.Response> getCategory(Long categoryId, Pageable pageable) {
        List<PlaceCategoryDto.Response> results = queryFactory
                .select(new QPlaceCategoryDto_Response(
                        place,
                        placeCategory
                        ))
                .from(place)
                .leftJoin(placeCategory).on(place.id.eq(placeCategory.place.id))
                .where(placeCategory.category.id.eq(categoryId))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }
}
