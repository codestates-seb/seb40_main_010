package com.main21.reserve.repository;

import com.main21.reserve.dto.QReserveDto_Response;
import com.main21.reserve.dto.ReserveDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.place.entity.QPlace.place;
import static com.main21.reserve.entity.QReserve.reserve;
import static com.main21.reserve.entity.Reserve.ReserveStatus.RESERVATION_CANCELED;

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
    public Page<ReserveDto.Response> getReservation(Long memberId, Pageable pageable) {
        List<ReserveDto.Response> results = queryFactory
                .select(new QReserveDto_Response(
                        reserve,
                        place
                ))
                .from(reserve)
                .leftJoin(place).on(reserve.placeId.eq(place.id))
                .where(reserve.memberId.eq(memberId),
                        reserve.status.ne(RESERVATION_CANCELED))
                .orderBy(reserve.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }
}
