package com.main21.bookmark.controller;

import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.repository.BookmarkRepository;
import com.main21.bookmark.service.BookmarkService;
import com.main21.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
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

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkService bookmarkService;

    // 북마크 전체 조회
    @GetMapping("/mypage/bookmark")
    public ResponseEntity getBookmark(@CookieValue( name = "memberId") Long memberId) {

        List<BookmarkDto.Response> list = bookmarkRepository.getBookmark(memberId);
        return new ResponseEntity<>(new SingleResponseDto(list), HttpStatus.OK);
    }


    // 북마크 추가 및 삭제
    @GetMapping("/bookmark/{place-id}")
    public ResponseEntity createBookmark(@PathVariable("place-id") Long placeId,
                                         @CookieValue(name = "memberId") Long memberId) {
        bookmarkService.createBookmark(placeId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
