package com.main10.domain.reserve.repository;

import com.main10.domain.reserve.dto.QReserveDto_Detail;
import com.main10.domain.reserve.dto.QReserveDto_Response;
import com.main10.domain.reserve.dto.ReserveDto;
import com.main10.domain.reserve.entity.Reserve;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityManager;
import java.util.List;
import static com.main10.domain.place.entity.QPlace.place;
import static com.main10.domain.reserve.entity.QReserve.reserve;
import static com.main10.domain.reserve.entity.Reserve.ReserveStatus.RESERVATION_CANCELED;

public class ReserveRepositoryImpl implements CustomReserveRepository {

    private final JPAQueryFactory queryFactory;

    public ReserveRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 마이페이지 예약 내역 조회 Query
     *
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

    /**
     * 상세페이지 예약된 시간 조회 메서드
     *
     * @param placeId 장소 식별자
     * @return List(ResponseDto.Detail)
     * @author mozzi327
     */
    public List<ReserveDto.Detail> getDetailReservationTime(Long placeId) {
        return queryFactory
                .select(new QReserveDto_Detail(
                        reserve.startTime,
                        reserve.endTime
                ))
                .from(reserve)
                .where(reserve.placeId.eq(placeId),
                        reserve.status.eq(Reserve.ReserveStatus.PAY_SUCCESS))
                .fetch();
    }
}
