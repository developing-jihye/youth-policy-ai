package com.youthpolicy.backend.policy.repository;

import com.youthpolicy.backend.policy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

	List<Policy> findAllByRegion(String region);
}
