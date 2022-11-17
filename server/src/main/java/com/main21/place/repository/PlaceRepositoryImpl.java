package com.main21.place.repository;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.dto.QPlaceCategoryDto_Response;
import com.main21.place.dto.QPlaceDto_Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.place.entity.QPlace.place;
import static com.main21.place.entity.QPlaceCategory.placeCategory;

public class PlaceRepositoryImpl implements CustomPlaceRepository{

    private final JPAQueryFactory queryFactory;

    public PlaceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * Pagination 적용한 공간 전체 조회 Querydsl
     * @param pageable
     * @return
     */
    @Override
    public Page<PlaceDto.Response> getPlacesPage(Pageable pageable) {
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

    /**
     * Slice 무한스크롤 공간 전체 조회 Querydsl
     * @param pageable
     * @return
     */
    @Override
    public Slice<PlaceDto.Response> getPlacesSlice(Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(place))
                .from(place)
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            results.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * Pagination 적용한 공간 카테고리 조회 Querydsl
     * @param categoryId
     * @param pageable
     * @return
     */
    @Override
    public Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable) {
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

    /**
     * Slice 무한스크롤 공간 카테고리 조회 Querydsl
     * @param categoryId
     * @param pageable
     * @return
     */
    @Override
    public Slice<PlaceCategoryDto.Response> getCategorySlice(Long categoryId, Pageable pageable) {
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
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            results.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 상세 검색(int )
     * @param searchDetail
     * @param pageable
     * @return
     */
    @Override
    public Page<PlaceDto.Response> getSearchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(
                        place
                ))
                .from(place)
                .where(place.charge.between(searchDetail.getStartCharge(), searchDetail.getEndCharge())
                        .or(place.maxCapacity.goe(searchDetail.getCapacity())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<PlaceDto.Response> getPlaceMypage(Long memberId, Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(
                        place
                ))
                .from(place)
                .where(
                        place.memberId.eq(memberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }
}