package com.main21.member.repository;

import com.main21.member.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    void deleteByMemberId(Long memberId);
}
