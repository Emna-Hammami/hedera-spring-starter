package com.spring.hederaspringstarter.hederaAccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.account.AccountInfo;
import com.spring.hederaspringstarter.hederaAccount.model.Account;
import com.spring.hederaspringstarter.hederaAccount.model.TransferHbar;
import com.spring.hederaspringstarter.hederaAccount.service.AccountService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
	@Autowired
	AccountService aS;

	public AccountController() {
		aS = new AccountService();
	}
	
	@PostMapping(path= "/create")
	@ApiImplicitParams({
		@ApiImplicitParam(
				name = "initialBalance", type = "long",
				example = "0", value = "add this much tinyBars to the user."
				)
	})
	public Account createAccount(@RequestParam(defaultValue = "0") long initialBalance) 
			throws HederaStatusException {
		return aS.createAccount(initialBalance);
	}
	
	@DeleteMapping(path="/delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "accountId", required = true, type = "String"),
        @ApiImplicitParam(name = "accountPrivateKey", required = true, type = "String")
	})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "success or failure")})
	public boolean deleteAccount(@RequestParam String accountId, @RequestParam String accountPrivateKey)
		throws HederaStatusException {
		return aS.deleteAccount(accountId, accountPrivateKey);
	}
	
	@GetMapping("/info/{accountId}")
	@ApiImplicitParam(name = "accountId", required = true, type = "String")
	public AccountInfo getAccountInfo(@PathVariable String accountId)
			throws HederaStatusException {
		return aS.getAccountInfo(accountId);
	}
	
	@GetMapping("/balance/{accountId}")
	@ApiImplicitParam(name = "accountId", required = true, type = "String")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Balance in tinyBars:")})
	public long getHbarBalance(@PathVariable String accountId)
			throws HederaStatusException {
		return aS.getHbarAccountBalance(accountId);
	}
	
	
	@PostMapping(path="/cryptoTransfer")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "sucess or failure")})
	public boolean transferHbar(@RequestBody TransferHbar request)
			throws HederaStatusException {
		return aS.transferHbars(request);
	}
	
	

}
