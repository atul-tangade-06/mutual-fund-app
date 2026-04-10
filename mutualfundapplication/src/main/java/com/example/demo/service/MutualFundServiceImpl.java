package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BuyRequest;
import com.example.demo.dto.SellRequest;
import com.example.demo.entity.Fund;
import com.example.demo.entity.Portfolio;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionType;
import com.example.demo.repository.FundRepository;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@AllArgsConstructor

public class MutualFundServiceImpl implements MutualFundService {

	private final FundRepository fundRepo;
	private final PortfolioRepository portfolioRepo;
	private final TransactionRepository txnRepo;

	public MutualFundServiceImpl(FundRepository fundRepo, PortfolioRepository portfolioRepo,
			TransactionRepository txnRepo) {
		super();
		this.fundRepo = fundRepo;
		this.portfolioRepo = portfolioRepo;
		this.txnRepo = txnRepo;
	}

	@Transactional
	public Object buy(BuyRequest request) {

		if (txnRepo.existsByIdempotencyKey(request.getIdempotencyKey()))
			throw new RuntimeException("DUPLICATE");

		Fund fund = fundRepo.findById(request.getFundId()).orElseThrow(() -> new RuntimeException("Fund not found"));

		BigDecimal units = request.getAmount().divide(fund.getNav(), 4, RoundingMode.HALF_UP);

		Transaction txn = new Transaction();
		txn.setUserId(request.getUserId());
		txn.setFundId(request.getFundId());
		txn.setType(TransactionType.BUY);
		txn.setAmount(request.getAmount());
		txn.setUnits(units);
		txn.setNav(fund.getNav());
		txn.setIdempotencyKey(request.getIdempotencyKey());

		txnRepo.save(txn);

		Portfolio portfolio = portfolioRepo.findByUserIdAndFundId(request.getUserId(), request.getFundId())
				.orElse(new Portfolio());

		portfolio.setUserId(request.getUserId());
		portfolio.setFundId(request.getFundId());
		portfolio.setTotalUnits(portfolio.getTotalUnits().add(units));
		portfolio.setInvestedAmount(portfolio.getInvestedAmount().add(request.getAmount()));

		portfolioRepo.save(portfolio);

		return Map.of("message", "Buy Successful", "units", units);
	}

	@Transactional
	public Object sell(SellRequest request) {

		if (txnRepo.existsByIdempotencyKey(request.getIdempotencyKey()))
			throw new RuntimeException("DUPLICATE");

		Portfolio portfolio = portfolioRepo.findByUserIdAndFundId(request.getUserId(), request.getFundId())
				.orElseThrow(() -> new RuntimeException("No holdings"));

		if (portfolio.getTotalUnits().compareTo(request.getUnits()) < 0)
			throw new RuntimeException("INSUFFICIENT");

		Fund fund = fundRepo.findById(request.getFundId()).orElseThrow(() -> new RuntimeException("Fund not found"));

		// ✅ Correct financial logic
		BigDecimal avgCost = portfolio.getInvestedAmount().divide(portfolio.getTotalUnits(), 4, RoundingMode.HALF_UP);

		BigDecimal costToReduce = avgCost.multiply(request.getUnits());

		BigDecimal amount = request.getUnits().multiply(fund.getNav());

		Transaction txn = new Transaction();
		txn.setUserId(request.getUserId());
		txn.setFundId(request.getFundId());
		txn.setType(TransactionType.SELL);
		txn.setUnits(request.getUnits());
		txn.setAmount(amount);
		txn.setNav(fund.getNav());
		txn.setIdempotencyKey(request.getIdempotencyKey());

		txnRepo.save(txn);

		portfolio.setTotalUnits(portfolio.getTotalUnits().subtract(request.getUnits()));
		portfolio.setInvestedAmount(portfolio.getInvestedAmount().subtract(costToReduce));

		portfolioRepo.save(portfolio);

		return Map.of("message", "Sell Successful", "amount", amount);
	}

	public List<Portfolio> getPortfolio(Long userId) {
		return portfolioRepo.findByUserId(userId);
	}
}
