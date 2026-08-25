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
import java.util.List;
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

	@Test
	void getAllReturnsPolicies() {
		LocalDate firstStartDate = LocalDate.of(2026, 8, 25);
		LocalDate firstEndDate = LocalDate.of(2026, 9, 30);
		Policy firstPolicy = policyService.register(new Policy(
				"첫 번째 전체 조회 정책",
				"취업",
				"첫 번째 청년정책기관",
				"전국",
				firstStartDate,
				firstEndDate,
				RecruitmentStatus.OPEN,
				"https://example.go.kr/policies/service-list-1"
		));

		LocalDate secondStartDate = LocalDate.of(2026, 10, 1);
		LocalDate secondEndDate = LocalDate.of(2026, 10, 31);
		Policy secondPolicy = policyService.register(new Policy(
				"두 번째 전체 조회 정책",
				"주거",
				"두 번째 청년정책기관",
				"서울",
				secondStartDate,
				secondEndDate,
				RecruitmentStatus.UPCOMING,
				"https://example.go.kr/policies/service-list-2"
		));

		Long firstPolicyId = firstPolicy.getId();
		Long secondPolicyId = secondPolicy.getId();
		assertThat(firstPolicyId).isNotNull();
		assertThat(secondPolicyId).isNotNull();

		entityManager.flush();
		entityManager.clear();

		List<Policy> policies = policyService.getAll();
		Policy foundFirstPolicy = policies.stream()
				.filter(policy -> firstPolicyId.equals(policy.getId()))
				.findFirst()
				.orElseThrow();
		Policy foundSecondPolicy = policies.stream()
				.filter(policy -> secondPolicyId.equals(policy.getId()))
				.findFirst()
				.orElseThrow();

		assertThat(foundFirstPolicy.getId()).isEqualTo(firstPolicyId);
		assertThat(foundFirstPolicy.getName()).isEqualTo("첫 번째 전체 조회 정책");
		assertThat(foundFirstPolicy.getCategory()).isEqualTo("취업");
		assertThat(foundFirstPolicy.getOrganization()).isEqualTo("첫 번째 청년정책기관");
		assertThat(foundFirstPolicy.getRegion()).isEqualTo("전국");
		assertThat(foundFirstPolicy.getApplicationStartDate()).isEqualTo(firstStartDate);
		assertThat(foundFirstPolicy.getApplicationEndDate()).isEqualTo(firstEndDate);
		assertThat(foundFirstPolicy.getRecruitmentStatus()).isEqualTo(RecruitmentStatus.OPEN);
		assertThat(foundFirstPolicy.getSourceUrl()).isEqualTo("https://example.go.kr/policies/service-list-1");

		assertThat(foundSecondPolicy.getId()).isEqualTo(secondPolicyId);
		assertThat(foundSecondPolicy.getName()).isEqualTo("두 번째 전체 조회 정책");
		assertThat(foundSecondPolicy.getCategory()).isEqualTo("주거");
		assertThat(foundSecondPolicy.getOrganization()).isEqualTo("두 번째 청년정책기관");
		assertThat(foundSecondPolicy.getRegion()).isEqualTo("서울");
		assertThat(foundSecondPolicy.getApplicationStartDate()).isEqualTo(secondStartDate);
		assertThat(foundSecondPolicy.getApplicationEndDate()).isEqualTo(secondEndDate);
		assertThat(foundSecondPolicy.getRecruitmentStatus()).isEqualTo(RecruitmentStatus.UPCOMING);
		assertThat(foundSecondPolicy.getSourceUrl()).isEqualTo("https://example.go.kr/policies/service-list-2");
	}
}
