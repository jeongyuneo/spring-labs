package com.jeongyuneo.querydsl;

import com.jeongyuneo.querydsl.entity.Member;
import com.jeongyuneo.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeongyuneo.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class QuerydslBasicTest {

    @Autowired
    EntityManager entityManager;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(entityManager);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
    }

    @Test
    void JPQL을_이용해_멤버1을_찾는다() {
        // given
        String query = "select m from Member m where m.username = :username";
        // when
        Member findMember = entityManager.createQuery(query, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void Querydsl을_이용해_멤버1을_찾는다() {
        // given
        // when
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void Querydsl을_이용해_나이가_10인_멤버1을_찾는다() {
        // given
        // when
        // and 연산자
        Member findMember1 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1")
                                .and(member.age.eq(10)))
                .fetchOne();
        // 파라미터 나열
        Member findMember2 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10))
                .fetchOne();
        // then
        assertThat(findMember1.getUsername()).isEqualTo("member1");
        assertThat(findMember1.getAge()).isEqualTo(10);

        assertThat(findMember2.getUsername()).isEqualTo("member1");
        assertThat(findMember2.getAge()).isEqualTo(10);
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 오름차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    void Querydsl을_이용해_정렬된_멤버를_조회한다() {
        // given
        entityManager.persist(new Member(null, 100));
        entityManager.persist(new Member("member5", 100));
        entityManager.persist(new Member("member6", 100));
        // when
        List<Member> findMembers = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();
        // then
        assertThat(findMembers.get(0).getUsername()).isEqualTo("member5");
        assertThat(findMembers.get(1).getUsername()).isEqualTo("member6");
        assertThat(findMembers.get(2).getUsername()).isNull();
    }
}