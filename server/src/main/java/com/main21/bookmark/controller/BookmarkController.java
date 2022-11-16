package com.main21.bookmark.controller;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.repository.BookmarkRepository;
import com.main21.bookmark.service.BookmarkService;
import com.main21.dto.MultiResponseDto;
import com.main21.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * Pagination 적용
     * 회원별 북마크 전체 조회
     * @param memberId
     * @param pageable
     * @return
     */
    @GetMapping("/bookmark")
    public ResponseEntity getBookmark(@CookieValue( name = "memberId") Long memberId,
                                      Pageable pageable) {

        Page<BookmarkDto.Response> pageBookmark = bookmarkService.getBookmark(memberId, pageable);
        List<BookmarkDto.Response> bookmark = pageBookmark.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(bookmark, pageBookmark), HttpStatus.OK);
    }


    /**
     * 북마크 추가 및 삭제
     * memberId, PlaceId로 Bookmark 가져오기 Query
     * 북마크가 존재하면 북마크에서 삭제하고, 북마크가 존재하지 않으면 북마크에 추가하기
     * @param placeId
     * @param memberId
     * @return
     */
    @GetMapping("/bookmark/{place-id}")
    public ResponseEntity createBookmark(@PathVariable("place-id") Long placeId,
                                         @CookieValue(name = "memberId") Long memberId) {
        bookmarkService.createBookmark(placeId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
