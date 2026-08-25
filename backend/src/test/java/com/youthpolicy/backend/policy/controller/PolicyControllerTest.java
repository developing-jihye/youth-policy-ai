package com.youthpolicy.backend.policy.controller;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.domain.RecruitmentStatus;
import com.youthpolicy.backend.policy.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PolicyService policyService;

	@Test
	void registerReturnsCreated() throws Exception {
		Policy registeredPolicy = createPolicyMock(1L);
		given(policyService.register(any(Policy.class))).willReturn(registeredPolicy);

		mockMvc.perform(post("/api/policies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(validRequestJson()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("청년 정책 Controller 테스트"))
				.andExpect(jsonPath("$.category").value("취업"))
				.andExpect(jsonPath("$.organization").value("청년정책기관"))
				.andExpect(jsonPath("$.region").value("전국"))
				.andExpect(jsonPath("$.applicationStartDate").value("2026-08-25"))
				.andExpect(jsonPath("$.applicationEndDate").value("2026-09-30"))
				.andExpect(jsonPath("$.recruitmentStatus").value("OPEN"))
				.andExpect(jsonPath("$.sourceUrl").value("https://example.go.kr/policies/controller-test"));

		ArgumentCaptor<Policy> policyCaptor = ArgumentCaptor.forClass(Policy.class);
		verify(policyService).register(policyCaptor.capture());
		Policy policy = policyCaptor.getValue();
		assertThat(policy.getName()).isEqualTo("청년 정책 Controller 테스트");
		assertThat(policy.getCategory()).isEqualTo("취업");
		assertThat(policy.getOrganization()).isEqualTo("청년정책기관");
		assertThat(policy.getRegion()).isEqualTo("전국");
		assertThat(policy.getApplicationStartDate()).isEqualTo(LocalDate.of(2026, 8, 25));
		assertThat(policy.getApplicationEndDate()).isEqualTo(LocalDate.of(2026, 9, 30));
		assertThat(policy.getRecruitmentStatus()).isEqualTo(RecruitmentStatus.OPEN);
		assertThat(policy.getSourceUrl()).isEqualTo("https://example.go.kr/policies/controller-test");
	}

	@Test
	void registerReturnsBadRequestWhenBeanValidationFails() throws Exception {
		String invalidRequest = validRequestJson().replace(
				"\"name\": \"청년 정책 Controller 테스트\"",
				"\"name\": \" \""
		);

		mockMvc.perform(post("/api/policies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(invalidRequest))
				.andExpect(status().isBadRequest());

		verifyNoInteractions(policyService);
	}

	@Test
	void registerReturnsBadRequestWhenApplicationDatesAreReversed() throws Exception {
		String reversedDatesRequest = validRequestJson()
				.replace("\"applicationStartDate\": \"2026-08-25\"", "\"applicationStartDate\": \"2026-10-01\"");

		mockMvc.perform(post("/api/policies")
					.contentType(MediaType.APPLICATION_JSON)
					.content(reversedDatesRequest))
				.andExpect(status().isBadRequest());

		verifyNoInteractions(policyService);
	}

	@Test
	void getAllReturnsOk() throws Exception {
		Policy firstPolicy = createPolicyMock(1L);
		Policy secondPolicy = createPolicyMock(2L);
		given(policyService.getAll()).willReturn(List.of(firstPolicy, secondPolicy));

		mockMvc.perform(get("/api/policies"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("청년 정책 Controller 테스트"))
				.andExpect(jsonPath("$[0].category").value("취업"))
				.andExpect(jsonPath("$[0].organization").value("청년정책기관"))
				.andExpect(jsonPath("$[0].region").value("전국"))
				.andExpect(jsonPath("$[0].applicationStartDate").value("2026-08-25"))
				.andExpect(jsonPath("$[0].applicationEndDate").value("2026-09-30"))
				.andExpect(jsonPath("$[0].recruitmentStatus").value("OPEN"))
				.andExpect(jsonPath("$[0].sourceUrl").value("https://example.go.kr/policies/controller-test"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value("청년 정책 Controller 테스트"))
				.andExpect(jsonPath("$[1].category").value("취업"))
				.andExpect(jsonPath("$[1].organization").value("청년정책기관"))
				.andExpect(jsonPath("$[1].region").value("전국"))
				.andExpect(jsonPath("$[1].applicationStartDate").value("2026-08-25"))
				.andExpect(jsonPath("$[1].applicationEndDate").value("2026-09-30"))
				.andExpect(jsonPath("$[1].recruitmentStatus").value("OPEN"))
				.andExpect(jsonPath("$[1].sourceUrl").value("https://example.go.kr/policies/controller-test"));

		verify(policyService).getAll();
	}

	@Test
	void getAllReturnsEmptyList() throws Exception {
		given(policyService.getAll()).willReturn(List.of());

		mockMvc.perform(get("/api/policies"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));

		verify(policyService).getAll();
	}

	@Test
	void getByIdReturnsOk() throws Exception {
		Policy policy = createPolicyMock(1L);
		given(policyService.getById(1L)).willReturn(policy);

		mockMvc.perform(get("/api/policies/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("청년 정책 Controller 테스트"))
				.andExpect(jsonPath("$.category").value("취업"))
				.andExpect(jsonPath("$.organization").value("청년정책기관"))
				.andExpect(jsonPath("$.region").value("전국"))
				.andExpect(jsonPath("$.applicationStartDate").value("2026-08-25"))
				.andExpect(jsonPath("$.applicationEndDate").value("2026-09-30"))
				.andExpect(jsonPath("$.recruitmentStatus").value("OPEN"))
				.andExpect(jsonPath("$.sourceUrl").value("https://example.go.kr/policies/controller-test"));

		verify(policyService).getById(1L);
	}

	@Test
	void getByIdReturnsNotFound() throws Exception {
		given(policyService.getById(999L))
				.willThrow(new NoSuchElementException("Policy not found: 999"));

		mockMvc.perform(get("/api/policies/{id}", 999L))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));

		verify(policyService).getById(999L);
	}

	private static Policy createPolicyMock(Long id) {
		Policy policy = mock(Policy.class);
		given(policy.getId()).willReturn(id);
		given(policy.getName()).willReturn("청년 정책 Controller 테스트");
		given(policy.getCategory()).willReturn("취업");
		given(policy.getOrganization()).willReturn("청년정책기관");
		given(policy.getRegion()).willReturn("전국");
		given(policy.getApplicationStartDate()).willReturn(LocalDate.of(2026, 8, 25));
		given(policy.getApplicationEndDate()).willReturn(LocalDate.of(2026, 9, 30));
		given(policy.getRecruitmentStatus()).willReturn(RecruitmentStatus.OPEN);
		given(policy.getSourceUrl()).willReturn("https://example.go.kr/policies/controller-test");
		return policy;
	}

	private static String validRequestJson() {
		return """
				{
				  "name": "청년 정책 Controller 테스트",
				  "category": "취업",
				  "organization": "청년정책기관",
				  "region": "전국",
				  "applicationStartDate": "2026-08-25",
				  "applicationEndDate": "2026-09-30",
				  "recruitmentStatus": "OPEN",
				  "sourceUrl": "https://example.go.kr/policies/controller-test"
				}
				""";
	}
}
