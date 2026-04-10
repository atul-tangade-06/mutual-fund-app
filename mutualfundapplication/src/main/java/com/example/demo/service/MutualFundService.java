package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.BuyRequest;
import com.example.demo.dto.SellRequest;
import com.example.demo.entity.Portfolio;

public interface MutualFundService {
	Object buy(BuyRequest request);

	Object sell(SellRequest request);

	List<Portfolio> getPortfolio(Long userId);
}