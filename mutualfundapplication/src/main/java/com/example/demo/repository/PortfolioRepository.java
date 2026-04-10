package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Portfolio;

import jakarta.persistence.LockModeType;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

	List<Portfolio> findByUserId(Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Portfolio> findByUserIdAndFundId(Long userId, Long fundId);
}
