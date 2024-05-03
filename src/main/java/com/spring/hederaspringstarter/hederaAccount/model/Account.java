package com.spring.hederaspringstarter.hederaAccount.model;

import java.io.Serializable;

import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;

public class Account implements Serializable {
	private String accountId;
	private String privateKey;
	private String publicKey;
	private String solidityAddress;
	
	public Account() {
		super();
	}

	public Account(AccountId accountId, Ed25519PrivateKey privateKey) {
		super();
		this.accountId = accountId.toString();
		this.privateKey = privateKey.toString();
		this.publicKey = privateKey.publicKey.toString();
		this.solidityAddress = accountId.toSolidityAddress();
	}

	public String getAccountId() {
		return accountId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getSolidityAddress() {
		return solidityAddress;
	}
	
	

}
