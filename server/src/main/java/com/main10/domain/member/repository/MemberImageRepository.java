package com.main10.domain.member.repository;

import com.main10.domain.member.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    void deleteByMemberId(Long memberId);
}
