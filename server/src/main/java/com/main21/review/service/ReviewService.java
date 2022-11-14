package com.main21.review.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
<<<<<<< Updated upstream
=======
import com.main21.member.repository.MemberRepository;
>>>>>>> Stashed changes
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

<<<<<<< Updated upstream
import java.util.Optional;
=======
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
>>>>>>> Stashed changes

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
<<<<<<< Updated upstream
=======
    private final MemberRepository memberRepository;

>>>>>>> Stashed changes
    public void createReview(ReviewDto.Post post,
                             Long memberId,
                             Long placeId) {
        Review review = Review.builder()
                .score(post.getScore())
                .comment(post.getComment())
                .memberId(memberId)
                .placeId(placeId).build();
        reviewRepository.save(review);
    }

<<<<<<< Updated upstream
    public void updateReview (Long reviewId, ReviewDto.Patch patch){
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        findReview.editReview(patch.getScore(),patch.getComment());
        reviewRepository.save(findReview);
    }

    public void deleteReview (Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
=======
    public void updateReview(Long reviewId, ReviewDto.Patch patch) {
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        findReview.editReview(patch.getScore(), patch.getComment());
        reviewRepository.save(findReview);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

//    public List<ReviewDto.Response> getDetailReviews(Long placeId) {
//        return reviewRepository.findAllById(placeId).stream()
//                .map(review -> {
//                    new findMember = memberRepository.findById()
//                    ReviewDto.Response
//                            .builder()
//                            .
//
//                        .build()}
//                )
//
//                .collect(Collectors.toList());
//    }
//}
>>>>>>> Stashed changes
