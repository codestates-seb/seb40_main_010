package com.main21.bookmark.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class BookmarkDto {

    @Getter
    public static class Response implements Comparable<Response> {
        private final Long bookmarkId;
        private final MultipartFile image;
        private final String title;
        private final String bookmarkUrl;
        private final double score;
        private final int charge;
        private final String address;

        @Builder
        public Response(Long bookmarkId,
                        MultipartFile image,
                        String title,
                        String bookmarkUrl,
                        double score,
                        int charge,
                        String address) {
            this.bookmarkId = bookmarkId;
            this.image = image;
            this.title = title;
            this.bookmarkUrl = bookmarkUrl;
            this.score = score;
            this.charge = charge;
            this.address = address;
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
