package com.youthpolicy.backend.policy.repository;

import com.youthpolicy.backend.policy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
