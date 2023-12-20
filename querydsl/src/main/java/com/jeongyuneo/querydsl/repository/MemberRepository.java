package com.jeongyuneo.querydsl.repository;

import com.jeongyuneo.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    List<Member> findByUsername(String username);
}
