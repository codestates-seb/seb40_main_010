package com.main21.bookmark.repository;

import com.main21.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, CustomBookmarkRepository {
}
