package com.youthpolicy.backend.policy.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PolicyJpaMappingTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void persistsAndFindsPolicy() {
		LocalDate startDate = LocalDate.of(2026, 8, 24);
		LocalDate endDate = LocalDate.of(2026, 9, 30);
		Policy policy = new Policy(
				"청년 정책 테스트",
				"취업",
				"청년정책기관",
				"전국",
				startDate,
				endDate,
				RecruitmentStatus.OPEN,
				"https://example.go.kr/policies/1"
		);

		entityManager.persist(policy);
		entityManager.flush();
		Long policyId = policy.getId();
		entityManager.clear();

		Policy foundPolicy = entityManager.find(Policy.class, policyId);

		assertThat(policyId).isNotNull();
		assertThat(foundPolicy).isNotNull();
		assertThat(foundPolicy.getId()).isEqualTo(policyId);
		assertThat(foundPolicy.getName()).isEqualTo("청년 정책 테스트");
		assertThat(foundPolicy.getCategory()).isEqualTo("취업");
		assertThat(foundPolicy.getOrganization()).isEqualTo("청년정책기관");
		assertThat(foundPolicy.getRegion()).isEqualTo("전국");
		assertThat(foundPolicy.getApplicationStartDate()).isEqualTo(startDate);
		assertThat(foundPolicy.getApplicationEndDate()).isEqualTo(endDate);
		assertThat(foundPolicy.getRecruitmentStatus()).isEqualTo(RecruitmentStatus.OPEN);
		assertThat(foundPolicy.getSourceUrl()).isEqualTo("https://example.go.kr/policies/1");
	}
}
