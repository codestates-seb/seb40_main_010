package com.main21.place.repository;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.dto.QPlaceCategoryDto_Response;
import com.main21.place.dto.QPlaceDto_Response;
import com.querydsl.core.types.dsl.BooleanExpression;
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
     * @author LeeGoh
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
      @author LeeGoh
     */
    /*
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
     */

    /**
     * Pagination 적용한 공간 카테고리 조회 Querydsl
     * @param categoryId
     * @param pageable
     * @return
      @author LeeGoh
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
      @author LeeGoh
     */
    /*
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
     */

    /**
     * 공간 최소 가격, 최대 가격, 인원수별 상세 검색 Query
     * @param searchDetail
     * @param pageable
     * @return
      @author LeeGoh
     */
    @Override
    public Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(
                        place
                ))
                .from(place)
                .where(place.charge.between(searchDetail.getStartCharge(), searchDetail.getEndCharge())
                        .or(place.maxCapacity.goe(searchDetail.getCapacity())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(place.id.desc())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    /**
     * 공간 전체 타이틀 검색 Query
     * @param titles
     * @param pageable
     * @return
     * @author LeeGoh
     */
    @Override
    public Page<PlaceDto.Response> searchTitle(List<String> titles, Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(
                        place
                ))
                .from(place)
                .where(searchTitleArray(titles, place.title.contains(titles.get(0))))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    /**
     * 공간 카테고리별 타이틀 검색 Query
     * @param categoryId
     * @param titles
     * @param pageable
     * @return
     @author LeeGoh
     */
    @Override
    public Page<PlaceCategoryDto.Response> searchCategoryTitle(Long categoryId, List<String> titles, Pageable pageable) {
        List<PlaceCategoryDto.Response> results = queryFactory
                .select(new QPlaceCategoryDto_Response(
                        place,
                        placeCategory
                ))
                .from(place)
                .leftJoin(placeCategory).on(place.id.eq(placeCategory.place.id))
                .where(placeCategory.category.id.eq(categoryId),
                        searchTitleArray(titles, place.title.contains(titles.get(0))))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    /**
     * 검색 단어의 수만큼 or문 추가하는 Query
     * 검색 단어가 하나라도 포함된 게시글 조회
     * @param titles
     * @param title
     * @return
     @author mozzi327
     */
    private BooleanExpression searchTitleArray(List<String> titles, BooleanExpression title) {
        for (int i = 1; i < titles.size(); i++) {
            title = title.or(place.title.contains(titles.get(i)));
        }
        return title;
    }

    private BooleanExpression eqCategoryId(Long categoryId) {
        return categoryId == 0 ? null : placeCategory.category.id.eq(categoryId);
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