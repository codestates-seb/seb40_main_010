package com.main21.bookmark.dto;

import com.main21.bookmark.entity.Bookmark;
import com.main21.place.entity.Place;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class BookmarkDto {

    @Getter
    public static class Response implements Comparable<Response> {
        private final Long bookmarkId;
        private final String image;
        private final String title;
        private final String bookmarkUrl;
        private final double score;
        private final int charge;
        private final String address;

        @Builder
        @QueryProjection
        public Response(Bookmark bookmark, Place place) {
            this.bookmarkId = bookmark.getId();
            this.image = place.getPlaceImages().get(0).getFilePath();
            this.title = place.getTitle();
            this.bookmarkUrl = bookmark.getBookmarkUrl();
            this.score = place.getScore();
            this.charge = place.getCharge();
            this.address = place.getAddress();
        }

        /**
         * 북마크 최신순 정렬 메서드
         * @param o Response
         * @return 1 or -1
         * @author mozzi327
         */
        @Override
        public int compareTo(Response o) {
            return (int) (this.bookmarkId - o.bookmarkId);
        }
    }
}
