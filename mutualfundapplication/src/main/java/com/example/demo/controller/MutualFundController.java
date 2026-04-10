package com.example.demo.controller;

 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BuyRequest;
import com.example.demo.dto.SellRequest;
import com.example.demo.service.MutualFundService;

import jakarta.validation.Valid;
 

@RestController
@RequestMapping("/secure/mutualfund")
public class MutualFundController {

	private final MutualFundService service;

	 
	public MutualFundController(MutualFundService service) {
		this.service = service;
	}

	@PostMapping("/buy")
	public ResponseEntity<?> buy(@RequestBody @Valid BuyRequest request) {
		return ResponseEntity.ok(service.buy(request));
	}

	@PostMapping("/sell")
	public ResponseEntity<?> sell(@RequestBody @Valid SellRequest request) {
		return ResponseEntity.ok(service.sell(request));
	}

	@GetMapping("/portfolio")
	public ResponseEntity<?> portfolio(@RequestParam Long userId) {
		return ResponseEntity.ok(service.getPortfolio(userId));
	}
}
