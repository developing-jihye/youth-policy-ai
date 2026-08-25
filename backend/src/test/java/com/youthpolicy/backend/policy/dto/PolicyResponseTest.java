package com.youthpolicy.backend.policy.dto;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PolicyResponseTest {

	@Test
	void convertsFromEntity() {
		LocalDate startDate = LocalDate.of(2026, 8, 25);
		LocalDate endDate = LocalDate.of(2026, 9, 30);
		Policy policy = new Policy(
				"청년 정책 응답 DTO 테스트",
				"금융",
				"청년정책기관",
				"전국",
				startDate,
				endDate,
				RecruitmentStatus.UPCOMING,
				"https://example.go.kr/policies/response-test"
		);

		PolicyResponse response = PolicyResponse.from(policy);

		assertThat(response.id()).isEqualTo(policy.getId());
		assertThat(response.name()).isEqualTo(policy.getName());
		assertThat(response.category()).isEqualTo(policy.getCategory());
		assertThat(response.organization()).isEqualTo(policy.getOrganization());
		assertThat(response.region()).isEqualTo(policy.getRegion());
		assertThat(response.applicationStartDate()).isEqualTo(policy.getApplicationStartDate());
		assertThat(response.applicationEndDate()).isEqualTo(policy.getApplicationEndDate());
		assertThat(response.recruitmentStatus()).isEqualTo(policy.getRecruitmentStatus());
		assertThat(response.sourceUrl()).isEqualTo(policy.getSourceUrl());
	}
}
