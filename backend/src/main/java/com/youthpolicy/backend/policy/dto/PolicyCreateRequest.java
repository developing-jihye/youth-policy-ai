package com.youthpolicy.backend.policy.dto;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PolicyCreateRequest(
		@NotBlank @Size(max = 255) String name,
		@NotBlank @Size(max = 100) String category,
		@NotBlank @Size(max = 255) String organization,
		@NotBlank @Size(max = 100) String region,
		LocalDate applicationStartDate,
		LocalDate applicationEndDate,
		@NotNull RecruitmentStatus recruitmentStatus,
		@NotBlank @Size(max = 2048) String sourceUrl
) {

	public Policy toEntity() {
		return new Policy(
				name,
				category,
				organization,
				region,
				applicationStartDate,
				applicationEndDate,
				recruitmentStatus,
				sourceUrl
		);
	}
}
