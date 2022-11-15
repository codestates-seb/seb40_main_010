package com.main21.review.repository;


import com.main21.member.entity.QMember;
import com.main21.review.dto.QReviewDto_Response;
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.member.entity.QMember.*;
import static com.main21.review.entity.QReview.*;

public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReviewDto.Response> getReviews(Long placeId) {
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
                .fetch();
        return result;
    }
}
