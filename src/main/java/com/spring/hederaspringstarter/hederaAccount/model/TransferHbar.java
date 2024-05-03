package com.spring.hederaspringstarter.hederaAccount.model;

import java.io.Serializable;

public class TransferHbar implements Serializable {
	private String toAccountId;
	private long amount;
	private String myAccountId;
	private String myPrivateKey;
	private String memo;
	
	public TransferHbar() {
		super();
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public long getAmount() {
		return amount;
	}

	public String getMyAccountId() {
		return myAccountId;
	}

	public String getMyPrivateKey() {
		return myPrivateKey;
	}

	public String getMemo() {
		return memo;
	}
	
	

}
