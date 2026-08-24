package com.youthpolicy.backend.policy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "policies")
public class Policy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "category", nullable = false, length = 100)
	private String category;

	@Column(name = "organization", nullable = false, length = 255)
	private String organization;

	@Column(name = "region", nullable = false, length = 100)
	private String region;

	@Column(name = "application_start_date")
	private LocalDate applicationStartDate;

	@Column(name = "application_end_date")
	private LocalDate applicationEndDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "recruitment_status", nullable = false, length = 20)
	private RecruitmentStatus recruitmentStatus;

	@Column(name = "source_url", nullable = false, length = 2048)
	private String sourceUrl;

	protected Policy() {
	}

	public Policy(
			String name,
			String category,
			String organization,
			String region,
			LocalDate applicationStartDate,
			LocalDate applicationEndDate,
			RecruitmentStatus recruitmentStatus,
			String sourceUrl
	) {
		validateApplicationDates(applicationStartDate, applicationEndDate);

		this.name = requireText(name, "name");
		this.category = requireText(category, "category");
		this.organization = requireText(organization, "organization");
		this.region = requireText(region, "region");
		this.applicationStartDate = applicationStartDate;
		this.applicationEndDate = applicationEndDate;
		this.recruitmentStatus = requireNonNull(recruitmentStatus, "recruitmentStatus");
		this.sourceUrl = requireText(sourceUrl, "sourceUrl");
	}

	private static String requireText(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " must not be blank");
		}
		return value;
	}

	private static <T> T requireNonNull(T value, String fieldName) {
		if (value == null) {
			throw new IllegalArgumentException(fieldName + " must not be null");
		}
		return value;
	}

	private static void validateApplicationDates(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("applicationStartDate must not be after applicationEndDate");
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getOrganization() {
		return organization;
	}

	public String getRegion() {
		return region;
	}

	public LocalDate getApplicationStartDate() {
		return applicationStartDate;
	}

	public LocalDate getApplicationEndDate() {
		return applicationEndDate;
	}

	public RecruitmentStatus getRecruitmentStatus() {
		return recruitmentStatus;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}
}
