package com.main21.bookmark.service;

import com.main21.bookmark.entity.Bookmark;
import com.main21.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    /**
     * if(findById(bookmarkId).isPresent()) -> delete
     * else -> save
     * @param placeId
     * @param memberId
     * @return
     */
    public void createBookmark(Long placeId, Long memberId) {
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