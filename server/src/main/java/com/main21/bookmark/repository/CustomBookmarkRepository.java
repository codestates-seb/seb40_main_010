package com.main21.bookmark.repository;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBookmarkRepository {
    Page<BookmarkDto.Response> getBookmark(Long memberId, Pageable pageable);
}
