package com.youthpolicy.backend.policy.service;

import com.youthpolicy.backend.policy.domain.Policy;
import com.youthpolicy.backend.policy.repository.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class PolicyService {

	private final PolicyRepository policyRepository;

	public PolicyService(PolicyRepository policyRepository) {
		this.policyRepository = policyRepository;
	}

	@Transactional
	public Policy register(Policy policy) {
		return policyRepository.save(policy);
	}

	@Transactional(readOnly = true)
	public Policy getById(Long id) {
		return policyRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Policy not found: " + id));
	}
}
