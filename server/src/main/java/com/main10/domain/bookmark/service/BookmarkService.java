package com.main10.domain.bookmark.service;

import com.main10.domain.bookmark.dto.BookmarkDto;
import com.main10.domain.bookmark.entity.Bookmark;
import com.main10.domain.bookmark.repository.BookmarkRepository;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.service.PlaceDbService;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
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
    private final PlaceDbService placeDbService;

    /**
     * Pagination 적용<br>
     * 회원별 북마크 전체 조회 메서드<br>
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page<BookmarkDto.Response>
     * @author LeeGoh
     */
    public Page<BookmarkDto.Response> getBookmark(Long memberId, Pageable pageable) {
        return bookmarkRepository.getBookmark(memberId, pageable);
    }

    /**
     * 북마크 추가 및 삭제
     * 북마크가 존재하면 북마크에서 삭제하고, 북마크가 존재하지 않으면 북마크에 추가하는 메서드
     *
     * @param placeId 공간 식별자
     * @param memberId 사용자 식별자
     * @return boolean
     * @author LeeGoh
     */
    public boolean createBookmark(Long placeId, Long memberId) {
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);

        if (findPlace.getMemberId().equals(memberId)) {
            throw new BusinessLogicException(ExceptionCode.HOST_CANNOT_BOOKMARK);
        }

        Optional<Bookmark> findBookmark = bookmarkRepository
                .findBookmarkByMemberIdAndPlaceId(memberId, placeId);

        if (findBookmark.isPresent()) {
            bookmarkRepository.delete(findBookmark.get());
            return false;
        } else {
            Bookmark bookmark = Bookmark.builder()
                    .memberId(memberId)
                    .placeId(placeId)
                    .build();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }
}
