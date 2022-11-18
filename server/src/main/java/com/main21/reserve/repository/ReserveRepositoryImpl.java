package com.main21.reserve.repository;

import com.main21.member.entity.QMember;
import com.main21.place.entity.QPlace;
import com.main21.reserve.dto.QReserveDto_Response;
import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.QReserve;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.place.entity.QPlace.place;
import static com.main21.reserve.entity.QReserve.reserve;

public class ReserveRepositoryImpl implements CustomReserveRepository{

    private final JPAQueryFactory queryFactory;

    public ReserveRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 마이페이지 예약 내역 조회 Query
     * @param memberId
     * @return
     * @author LeeGoh
     */
    @Override
    public List<ReserveDto.Response> getReservation(Long memberId) {
        List<ReserveDto.Response> results = queryFactory
                .select(new QReserveDto_Response(
                        reserve,
                        place
                ))
                .from(reserve)
                .leftJoin(place).on(reserve.placeId.eq(place.id))
                .where(reserve.memberId.eq(memberId))
                .orderBy(reserve.id.desc())
                .fetch();

        return results;
    }
}
