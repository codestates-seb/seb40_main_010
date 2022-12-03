package com.main10.domain.place.repository;

import com.main10.domain.place.dto.PlaceCategoryDto;
import com.main10.domain.place.dto.PlaceDto;
import com.main10.domain.place.dto.QPlaceCategoryDto_Response;
import com.main10.domain.place.dto.QPlaceDto_Response;
import com.main10.domain.place.entity.Place;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main10.domain.place.entity.QPlace.place;
import static com.main10.domain.place.entity.QPlaceCategory.placeCategory;

public class PlaceRepositoryImpl implements CustomPlaceRepository{

    private final JPAQueryFactory queryFactory;

    public PlaceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * Pagination 적용한 공간 전체 조회 Querydsl
     *
     * @param pageable 페이지 정보
     * @return Page<PlaceDto.Response>
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
     * Mbti 조회를 위한 단건 조회 QueryDsl
     *
     * @param placeId 장소 식별자
     * @return PlaceDto.Response
     * @author mozzi327
     */
    @Override
    public PlaceDto.Response getPlace(Long placeId) {
        return queryFactory
                .select(new QPlaceDto_Response(place))
                .from(place)
                .where(place.id.eq(placeId))
                .fetchOne();
    }

    /**
     * Slice 무한스크롤 공간 전체 조회 Querydsl
     *
     * @param pageable 페이지 정보
     * @return Slice<PlaceDto.Response>
     * @author LeeGoh
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
     *
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return Page<PlaceCategoryDto.Response>
     * @author LeeGoh
     */
    @Override
    public Page<PlaceCategoryDto.Response> getCategoryPage(Long categoryId, Pageable pageable) {
        List<PlaceCategoryDto.Response> results = queryFactory
                .select(new QPlaceCategoryDto_Response(
                        place,
                        placeCategory.id
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
     * 테스트
     * @param categoryId
     * @return
     */
    @Override
    public List<Place> getCategoryPageTest(Long categoryId) {
        List<Place> results = queryFactory
                .selectFrom(place)
                .leftJoin(placeCategory).on(place.id.eq(placeCategory.place.id))
                .where(placeCategory.category.id.eq(categoryId))
                .orderBy(place.id.desc())
                .fetch();
        return results;
    }

    /**
     * Slice 무한스크롤 공간 카테고리 조회 Querydsl
     *
     * @param categoryId 카테고리 식별자
     * @param pageable 페이지 정보
     * @return Slice<PlaceCategoryDto.Response>
     * @author LeeGoh
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
     *
     * @param searchDetail 상세 검색 항목
     * @param pageable 페이지 정보
     * @return Page<PlaceDto.Response>
     * @author LeeGoh
     */
    @Override
    public Page<PlaceDto.Response> searchDetail(PlaceDto.SearchDetail searchDetail, Pageable pageable) {
        List<PlaceDto.Response> results = queryFactory
                .select(new QPlaceDto_Response(place))
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
     *
     * @param titles 검색 키워드
     * @param pageable 페이지 정보
     * @return Page<PlaceDto.Response>
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
     *
     * @param categoryId 카테고리 식별자
     * @param titles 검색 키워드
     * @param pageable 페이지 정보
     * @return Page<PlaceCategoryDto.Response>
     * @author LeeGoh
     */
    @Override
    public Page<PlaceCategoryDto.Response> searchCategoryTitle(Long categoryId, List<String> titles, Pageable pageable) {
        List<PlaceCategoryDto.Response> results = queryFactory
                .select(new QPlaceCategoryDto_Response(
                        place,
                        placeCategory.id
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
     * 검색 단어의 수만큼 or문 추가하는 Query<br>
     * 검색 단어가 하나라도 포함된 게시글 조회
     *
     * @param titles 검색 키워드 리스트
     * @param title 검색 첫 번째 키워드
     * @return BooleanExpression
     * @author mozzi327
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

    /**
     * 검색 단어의 수만큼 or문 추가하는 Query<br>
     * 검색 단어가 하나라도 포함된 게시글 조회
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page<PlaceDto.Response>
     * @author mozzi327
     */
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