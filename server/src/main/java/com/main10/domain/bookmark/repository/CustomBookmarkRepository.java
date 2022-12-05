package com.main10.domain.bookmark.repository;

import com.main10.domain.bookmark.dto.BookmarkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBookmarkRepository {
    Page<BookmarkDto.Response> getBookmark(Long memberId, Pageable pageable);
}
