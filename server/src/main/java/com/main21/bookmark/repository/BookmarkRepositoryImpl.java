package com.main21.bookmark.repository;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.dto.QBookmarkDto_Response;
import com.main21.bookmark.entity.Bookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.bookmark.entity.QBookmark.bookmark;
import static com.main21.place.entity.QPlace.place;

public class BookmarkRepositoryImpl implements CustomBookmarkRepository{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * Pagination 적용
     * 회원별 북마크 전체 조회 Query
     * @param memberId
     * @return
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
                .where(
                        bookmark.memberId.eq(memberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);

    }

}