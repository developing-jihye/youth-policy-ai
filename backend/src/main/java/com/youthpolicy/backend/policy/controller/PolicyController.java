package com.youthpolicy.backend.policy.controller;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.dto.PolicyCreateRequest;
import com.youthpolicy.backend.policy.dto.PolicyResponse;
import com.youthpolicy.backend.policy.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

	private final PolicyService policyService;

	public PolicyController(PolicyService policyService) {
		this.policyService = policyService;
	}

	@PostMapping
	public ResponseEntity<PolicyResponse> register(@Valid @RequestBody PolicyCreateRequest request) {
		Policy registeredPolicy = policyService.register(request.toEntity());

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(PolicyResponse.from(registeredPolicy));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PolicyResponse> getById(@PathVariable("id") Long id) {
		Policy policy = policyService.getById(id);

		return ResponseEntity.ok(PolicyResponse.from(policy));
	}

	@GetMapping
	public ResponseEntity<List<PolicyResponse>> getAll(
			@RequestParam(name = "region", required = false) String region
	) {
		List<Policy> policies;
		if (region == null) {
			policies = policyService.getAll();
		} else {
			if (region.isBlank()) {
				throw new IllegalArgumentException("region must not be blank");
			}
			policies = policyService.getByRegion(region.trim());
		}

		List<PolicyResponse> responses = policies.stream()
				.map(PolicyResponse::from)
				.toList();

		return ResponseEntity.ok(responses);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void handleNotFound() {
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	void handleBadRequest() {
	}
}
