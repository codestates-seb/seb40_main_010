package com.main21.review.repository;


import com.main21.review.dto.QReviewDto_MyPage;
import com.main21.review.dto.QReviewDto_Response;
import com.main21.review.dto.ReviewDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.member.entity.QMember.*;
import static com.main21.place.entity.QPlace.place;
import static com.main21.review.entity.QReview.*;

public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }



    /**
     * 상세페이지 리뷰 조회
     * @param placeId
     * @param pageable
     * @return
     * @author Quartz614
     */

    @Override
    public Page<ReviewDto.Response> getReviews(Long placeId, Pageable pageable) {
        List<ReviewDto.Response> result = queryFactory
                .select(new QReviewDto_Response(
                        review,
                        member
                ))

                .from(review)
                .leftJoin(member).on(review.memberId.eq(member.id))
                .where(
                        review.placeId.eq(placeId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);

    }
    /**
     * 마이페이지에서 작성한 리뷰 조회
     * @param placeId
     * @param pageable
     * @return
     * @author Quartz614
     */
    @Override
    public Page<ReviewDto.MyPage> getReviewsMypage(Long memberId, Pageable pageable) {
        List<ReviewDto.MyPage> results = queryFactory
                .select(new QReviewDto_MyPage(
                        review,
                        place
                ))
                .from(review)
                .leftJoin(place).on(review.memberId.eq(place.memberId))
                .where(
                        review.memberId.eq(memberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.id.desc())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }
}
