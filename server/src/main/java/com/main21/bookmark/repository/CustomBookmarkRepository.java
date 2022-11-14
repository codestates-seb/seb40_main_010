package com.main21.bookmark.repository;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.entity.Bookmark;

import java.util.List;

public interface CustomBookmarkRepository {
    Bookmark findBookmark(Long placeId, Long memberId);
    Boolean exist(Long placeId, Long memberId);
    List<BookmarkDto.Response> getBookmark(Long memberId);
}
