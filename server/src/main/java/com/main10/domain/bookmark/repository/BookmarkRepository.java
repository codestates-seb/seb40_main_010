package com.main10.domain.bookmark.repository;

import com.main10.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, CustomBookmarkRepository {
    Optional<Bookmark> findBookmarkByMemberIdAndPlaceId(Long memberId, Long PlaceId);
    List<Bookmark> deleteAllByPlaceId(Long placeId);
}
