package com.youthpolicy.backend.policy.service;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PolicyServiceTest {

	@Autowired
	private PolicyService policyService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void registerAndGetById() {
		LocalDate startDate = LocalDate.of(2026, 8, 24);
		LocalDate endDate = LocalDate.of(2026, 9, 30);
		Policy policy = new Policy(
				"청년 정책 Service 테스트",
				"교육",
				"청년정책기관",
				"전국",
				startDate,
				endDate,
				RecruitmentStatus.UPCOMING,
				"https://example.go.kr/policies/service-test"
		);

		Policy registeredPolicy = policyService.register(policy);
		Long policyId = registeredPolicy.getId();
		assertThat(policyId).isNotNull();

		entityManager.flush();
		entityManager.clear();

		Policy foundPolicy = policyService.getById(policyId);

		assertThat(foundPolicy.getId()).isEqualTo(policyId);
		assertThat(foundPolicy.getName()).isEqualTo("청년 정책 Service 테스트");
		assertThat(foundPolicy.getCategory()).isEqualTo("교육");
		assertThat(foundPolicy.getOrganization()).isEqualTo("청년정책기관");
		assertThat(foundPolicy.getRegion()).isEqualTo("전국");
		assertThat(foundPolicy.getApplicationStartDate()).isEqualTo(startDate);
		assertThat(foundPolicy.getApplicationEndDate()).isEqualTo(endDate);
		assertThat(foundPolicy.getRecruitmentStatus()).isEqualTo(RecruitmentStatus.UPCOMING);
		assertThat(foundPolicy.getSourceUrl()).isEqualTo("https://example.go.kr/policies/service-test");
	}

	@Test
	void getByIdNotFound() {
		assertThatThrownBy(() -> policyService.getById(-1L))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("Policy not found: -1");
	}
}
