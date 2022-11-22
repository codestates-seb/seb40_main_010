package com.main21.bookmark.service;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.entity.Bookmark;
import com.main21.bookmark.repository.BookmarkRepository;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RedisUtils redisUtils;

    /**
     * Pagination 적용
     * 회원별 북마크 전체 조회 메서드
     *
     * @param refreshToken
     * @param pageable
     * @return
     */
    public Page<BookmarkDto.Response> getBookmark(String refreshToken, Pageable pageable) {
        Long memberId = redisUtils.getId(refreshToken);
        return bookmarkRepository.getBookmark(memberId, pageable);
    }

    /**
     * 북마크 추가 및 삭제
     * 북마크가 존재하면 북마크에서 삭제하고, 북마크가 존재하지 않으면 북마크에 추가하는 메서드
     *
     * @param placeId
     * @param refreshToken
     * @return
     */
    public void createBookmark(Long placeId, String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);
        Bookmark bookmark = Bookmark.builder()
                .memberId(memberId)
                .placeId(placeId)
                .build();

        Optional<Bookmark> findBookmark = bookmarkRepository.findBookmarkByMemberIdAndPlaceId(placeId, memberId);

        if (findBookmark.isPresent()) {
            bookmarkRepository.delete(findBookmark.get());
        } else bookmarkRepository.save(bookmark);
    }
}
