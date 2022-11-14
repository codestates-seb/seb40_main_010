package com.main21.bookmark.repository;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.dto.QBookmarkDto_Response;
import com.main21.bookmark.entity.Bookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.main21.bookmark.entity.QBookmark.bookmark;
import static com.main21.place.entity.QPlace.place;

public class BookmarkRepositoryImpl implements CustomBookmarkRepository{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Bookmark findBookmark(Long placeId, Long memberId) {
        return queryFactory
                .select(bookmark)
                .from(bookmark)
                .where(bookmark.placeId.eq(placeId),
                        bookmark.memberId.eq(memberId))
                .fetchFirst();
    }

//    @Override
//    public Boolean exist(Long placeId, Long memberId) {
//        Integer fetchOne = queryFactory
//                .selectOne()
//                .from(bookmark)
//                .where(bookmark.placeId.eq(placeId),
//                        bookmark.memberId.eq(memberId))
//                .fetchFirst();
//        return fetchOne != null;
//    }

    @Override
    public List<BookmarkDto.Response> getBookmark(Long memberId) {
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
                .fetch();

        return results;

    }

}
