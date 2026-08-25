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
import java.util.List;

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

	@Test
	void findsPoliciesByRegion() {
		Policy seoulPolicy = policyRepository.save(new Policy(
				"서울 지역 Repository 정책",
				"취업",
				"서울 청년정책기관",
				"서울",
				LocalDate.of(2026, 8, 25),
				LocalDate.of(2026, 9, 30),
				RecruitmentStatus.OPEN,
				"https://example.go.kr/policies/repository-seoul"
		));
		Policy busanPolicy = policyRepository.save(new Policy(
				"부산 지역 Repository 정책",
				"주거",
				"부산 청년정책기관",
				"부산",
				LocalDate.of(2026, 10, 1),
				LocalDate.of(2026, 10, 31),
				RecruitmentStatus.UPCOMING,
				"https://example.go.kr/policies/repository-busan"
		));

		policyRepository.flush();
		Long seoulPolicyId = seoulPolicy.getId();
		Long busanPolicyId = busanPolicy.getId();
		entityManager.clear();

		List<Policy> policies = policyRepository.findAllByRegion("서울");

		assertThat(policies)
				.extracting(Policy::getId)
				.contains(seoulPolicyId)
				.doesNotContain(busanPolicyId);
		assertThat(policies).allMatch(policy -> policy.getRegion().equals("서울"));
	}
}
