package com.main10.domain.bookmark.controller;

import com.main10.domain.bookmark.dto.BookmarkDto;
import com.main10.domain.bookmark.service.BookmarkService;
import com.main10.domain.dto.MultiResponseDto;
import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * Pagination 적용
     * 회원별 북마크 전체 조회
     *
     * @param pageable 페이지 정보
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/bookmark")
    public ResponseEntity<MultiResponseDto<?>> getBookmark(Authentication authentication,
                                      Pageable pageable) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Page<BookmarkDto.Response> pageBookmark = bookmarkService.getBookmark(token.getId(), pageable);
        List<BookmarkDto.Response> bookmark = pageBookmark.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(bookmark, pageBookmark));
    }


    /**
     * 북마크 추가 및 삭제<br>
     * memberId, PlaceId로 Bookmark 가져오기 Query<br>
     * 북마크가 존재하면 북마크에서 삭제하고, 북마크가 존재하지 않으면 북마크에 추가하기<br>
     *
     * @param placeId 공간 식별자
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/bookmark/{place-id}")
    public ResponseEntity<Boolean> createBookmark(@PathVariable("place-id") Long placeId,
                                         Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        boolean bookmark = bookmarkService.createBookmark(placeId, token.getId());
        return ResponseEntity.ok(bookmark);
    }
}
