package com.youthpolicy.backend.policy.repository;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PolicyRepositoryTest {

	@Autowired
	private PolicyRepository policyRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void savesAndFindsPolicy() {
		LocalDate startDate = LocalDate.of(2026, 8, 24);
		LocalDate endDate = LocalDate.of(2026, 9, 30);
		Policy policy = new Policy(
				"청년 정책 Repository 테스트",
				"주거",
				"청년정책기관",
				"전국",
				startDate,
				endDate,
				RecruitmentStatus.OPEN,
				"https://example.go.kr/policies/repository-test"
		);

		Policy savedPolicy = policyRepository.save(policy);
		policyRepository.flush();
		Long policyId = savedPolicy.getId();
		entityManager.clear();

		Policy foundPolicy = policyRepository.findById(policyId).orElseThrow();

		assertThat(policyId).isNotNull();
		assertThat(foundPolicy.getId()).isEqualTo(policyId);
		assertThat(foundPolicy.getName()).isEqualTo("청년 정책 Repository 테스트");
		assertThat(foundPolicy.getCategory()).isEqualTo("주거");
		assertThat(foundPolicy.getOrganization()).isEqualTo("청년정책기관");
		assertThat(foundPolicy.getRegion()).isEqualTo("전국");
		assertThat(foundPolicy.getApplicationStartDate()).isEqualTo(startDate);
		assertThat(foundPolicy.getApplicationEndDate()).isEqualTo(endDate);
		assertThat(foundPolicy.getRecruitmentStatus()).isEqualTo(RecruitmentStatus.OPEN);
		assertThat(foundPolicy.getSourceUrl()).isEqualTo("https://example.go.kr/policies/repository-test");
	}
}
