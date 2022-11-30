package com.main10.global.batch.repository;

import com.main10.global.batch.entity.MbtiCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main10.global.batch.entity.QMbtiCount.mbtiCount;

public class CustomBestRepositoryImpl implements CustomBestRepository {
    private final JPAQueryFactory queryFactory;

    public CustomBestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    @Transactional
    public List<MbtiCount> calcBest(String mbtiValue) {

        List<MbtiCount> mbtiCounts = queryFactory
                .selectFrom(mbtiCount)
                .where(mbtiCount.mbti.eq(mbtiValue))
                .orderBy(mbtiCount.totalCount.desc())
                .limit(4).fetch();


//        mbtiCounts
//                .forEach(mbtiCount ->
//                        queryFactory.insert(best).columns(
//                                        best.mbti,
//                                        best.placeId,
//                                        best.totalCount
//                                ).values(mbtiValue,
//                                        mbtiCount.getPlaceId(),
//                                        mbtiCount.getTotalCount())
//                                .execute()
//                );

//        mbtiCounts
//                .forEach(mbtiCount ->
//                        queryFactory.update(best)
//                                .set(best.mbti, mbtiValue)
//                                .set(best.placeId, mbtiCount.getPlaceId())
//                                .set(best.totalCount, mbtiCount.getTotalCount())
//                                .execute()
//                );
        return mbtiCounts;
    }
}
