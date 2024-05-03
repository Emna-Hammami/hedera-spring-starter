package com.spring.hederaspringstarter.hederaAccount.service;

import org.springframework.stereotype.Service;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.account.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.account.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.account.AccountDeleteTransaction;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.AccountInfo;
import com.hedera.hashgraph.sdk.account.AccountInfoQuery;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.spring.hederaspringstarter.hederaAccount.model.Account;
import com.spring.hederaspringstarter.hederaAccount.model.TransferHbar;
import com.spring.hederaspringstarter.utilities.EnvUtils;
import com.spring.hederaspringstarter.utilities.HederaClient;

@Service
public class AccountService {
	public Client client = HederaClient.getHederaClientInstance();
	
	public Account createAccount(long initialBalance) throws HederaStatusException {
		var newAccountPrivateKey = Ed25519PrivateKey.generate();
		var newAccountPublicKey = newAccountPrivateKey.publicKey;
		
		TransactionId txId = new AccountCreateTransaction()
				.setKey(newAccountPublicKey)
				.setInitialBalance(Hbar.fromTinybar(1000)).execute(client);
		AccountId newAccountId = txId.getReceipt(client).getAccountId();
		System.out.println("New account ID is: " +newAccountId);
		
		Account res = new Account(newAccountId, newAccountPrivateKey);
		return res;
	}
	
	public boolean deleteAccount(String accountId, String accountPrivateKey) throws HederaStatusException {
		new AccountDeleteTransaction().setDeleteAccountId(AccountId.fromString(accountId))
			.setTransferAccountId(EnvUtils.getOperatorId())
			.build(client)
			.sign(Ed25519PrivateKey.fromString(accountPrivateKey))
			.execute(client)
			.getReceipt(client);
		System.out.println("Deleted : "+accountId);
		return true;
	}
	
	public AccountInfo getAccountInfo(String accountId) throws HederaStatusException {
		long cost = new AccountInfoQuery().setAccountId(AccountId.fromString(accountId))
				.getCost(client);
		AccountInfo info = new AccountInfoQuery()
				.setAccountId(AccountId.fromString(accountId)).setQueryPayment(cost + cost / 50)
				.execute(client);
		System.out.println("Info : "+info);
		return info;
	}
	
	public long getHbarAccountBalance(String id) throws HederaStatusException {
		Hbar balance = new AccountBalanceQuery()
				.setAccountId(AccountId.fromString(id))
				.execute(client);
		System.out.println("Account balance: " +balance);
		return balance.asTinybar();
	}
	
	public boolean transferHbars(TransferHbar request) throws HederaStatusException {
		Client _client;
		AccountId sender;
		AccountId receiver = AccountId.fromString(request.getToAccountId());
		String memo = (request.getMemo()== null ? "" : request.getMemo());
		long amount = request.getAmount();
		if (request.getMyAccountId()==null && request.getMyPrivateKey()==null) {
			_client = HederaClient.getHederaClientInstance();
			sender = EnvUtils.getOperatorId();
		} else {
			_client = HederaClient.makeNewClient(request.getMyAccountId(), request.getMyPrivateKey());
			sender = AccountId.fromString(request.getMyAccountId());
		}
		new CryptoTransferTransaction().addSender(sender, amount)
			.addRecipient(receiver, amount)
			.setTransactionMemo(memo).execute(_client);
		System.out.println("Transferred : " + amount );
		return true;
	}

}
