package com.jeongyuneo.springsecurity.member.repository;

import com.jeongyuneo.springsecurity.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}
