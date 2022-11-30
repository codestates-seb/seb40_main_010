package com.main10.domain.bookmark.controller;

import com.main10.domain.bookmark.dto.BookmarkDto;
import com.main10.domain.bookmark.service.BookmarkService;
import com.main10.domain.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.main10.domain.member.utils.AuthConstant.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * Pagination 적용
     * 회원별 북마크 전체 조회
     *
     * @param refreshToken 리프래시 토큰
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/bookmark")
    public ResponseEntity getBookmark(@RequestHeader(REFRESH_TOKEN) String refreshToken,
                                      Pageable pageable) {

        Page<BookmarkDto.Response> pageBookmark = bookmarkService.getBookmark(refreshToken, pageable);
        List<BookmarkDto.Response> bookmark = pageBookmark.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(bookmark, pageBookmark), HttpStatus.OK);
    }


    /**
     * 북마크 추가 및 삭제<br>
     * memberId, PlaceId로 Bookmark 가져오기 Query<br>
     * 북마크가 존재하면 북마크에서 삭제하고, 북마크가 존재하지 않으면 북마크에 추가하기<br>
     *
     * @param placeId 공간 식별자
     * @param refreshToken 리프래시 토큰
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/bookmark/{place-id}")
    public ResponseEntity createBookmark(@PathVariable("place-id") Long placeId,
                                         @RequestHeader(REFRESH_TOKEN) String refreshToken) {
        boolean bookmark = bookmarkService.createBookmark(placeId, refreshToken);
        return ResponseEntity.ok(bookmark);
    }
}
