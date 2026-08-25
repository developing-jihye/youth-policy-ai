package com.youthpolicy.backend.policy.dto;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PolicyCreateRequestTest {

	@Test
	void convertsToEntity() {
		LocalDate startDate = LocalDate.of(2026, 8, 25);
		LocalDate endDate = LocalDate.of(2026, 9, 30);
		PolicyCreateRequest request = new PolicyCreateRequest(
				"청년 정책 DTO 테스트",
				"복지",
				"청년정책기관",
				"전국",
				startDate,
				endDate,
				RecruitmentStatus.OPEN,
				"https://example.go.kr/policies/dto-test"
		);

		Policy policy = request.toEntity();

		assertThat(policy.getId()).isNull();
		assertThat(policy.getName()).isEqualTo(request.name());
		assertThat(policy.getCategory()).isEqualTo(request.category());
		assertThat(policy.getOrganization()).isEqualTo(request.organization());
		assertThat(policy.getRegion()).isEqualTo(request.region());
		assertThat(policy.getApplicationStartDate()).isEqualTo(request.applicationStartDate());
		assertThat(policy.getApplicationEndDate()).isEqualTo(request.applicationEndDate());
		assertThat(policy.getRecruitmentStatus()).isEqualTo(request.recruitmentStatus());
		assertThat(policy.getSourceUrl()).isEqualTo(request.sourceUrl());
	}

	@Test
	void rejectsReversedApplicationDatesWhenConvertingToEntity() {
		PolicyCreateRequest request = new PolicyCreateRequest(
				"청년 정책 DTO 테스트",
				"복지",
				"청년정책기관",
				"전국",
				LocalDate.of(2026, 9, 30),
				LocalDate.of(2026, 8, 25),
				RecruitmentStatus.OPEN,
				"https://example.go.kr/policies/dto-test"
		);

		assertThatThrownBy(request::toEntity)
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("applicationStartDate must not be after applicationEndDate");
	}
}
