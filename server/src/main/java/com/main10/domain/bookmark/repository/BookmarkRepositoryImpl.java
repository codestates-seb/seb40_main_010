package com.main10.domain.bookmark.repository;

import com.main10.domain.bookmark.dto.BookmarkDto;
import com.main10.domain.bookmark.dto.QBookmarkDto_Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main10.domain.bookmark.entity.QBookmark.bookmark;
import static com.main10.domain.place.entity.QPlace.place;

public class BookmarkRepositoryImpl implements CustomBookmarkRepository{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * Pagination 적용
     *
     * 회원별 북마크 전체 조회 Query
     * @param memberId 사용자 식별자
     * @return Page<BookmarkDto.Response>
     * @author LeeGoh
     */
    @Override
    public Page<BookmarkDto.Response> getBookmark(Long memberId, Pageable pageable) {
        List<BookmarkDto.Response> results = queryFactory
                .select(new QBookmarkDto_Response(
                        bookmark,
                        place
                ))
                .from(bookmark)
                .leftJoin(place).on(bookmark.placeId.eq(place.id))
                .where(bookmark.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookmark.id.desc())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

}
