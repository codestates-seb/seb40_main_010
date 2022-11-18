package com.main21.review.repository;


import com.main21.review.dto.QReviewDto_Response;
import com.main21.review.dto.ReviewDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.member.entity.QMember.*;
import static com.main21.review.entity.QReview.*;
import static com.querydsl.jpa.JPAExpressions.select;

public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }



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

    @Override
    public Page<ReviewDto.Response> getReviewsMypage(Long memberId, Pageable pageable) {
        List<ReviewDto.Response> results = queryFactory
                .select(new QReviewDto_Response(
                        review,
                        member
                ))
                .from(review)
                .leftJoin(member).on(review.memberId.eq(member.id))
                .where(
                        review.memberId.eq(memberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }
}
