package com.youthpolicy.backend.policy.dto;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;

import java.time.LocalDate;

public record PolicyResponse(
		Long id,
		String name,
		String category,
		String organization,
		String region,
		LocalDate applicationStartDate,
		LocalDate applicationEndDate,
		RecruitmentStatus recruitmentStatus,
		String sourceUrl
) {

	public static PolicyResponse from(Policy policy) {
		return new PolicyResponse(
				policy.getId(),
				policy.getName(),
				policy.getCategory(),
				policy.getOrganization(),
				policy.getRegion(),
				policy.getApplicationStartDate(),
				policy.getApplicationEndDate(),
				policy.getRecruitmentStatus(),
				policy.getSourceUrl()
		);
	}
}
