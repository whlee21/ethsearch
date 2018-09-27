package io.blocko.ethsearch.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import io.blocko.ethsearch.config.ApplicationProperties;
import io.blocko.ethsearch.contract.EthBoard;

@Service
@Transactional
public class PostEventService {

    private final Logger log = LoggerFactory.getLogger(PostEventService.class);

    private String PRIVATE_KEY = "0x81bd7f6498d0642e5900b9cde860180aabe4b5d275617a15dace4ccc05a18207";
    // private String PRIVATE_KEY = "742a02a084659d55ac8e4c26ffa6fa9161712d5b1af1f864e145c937651c767c";
    private BigInteger GAS_LIMIT = BigInteger.valueOf(80_000_000L);
    private BigInteger GAS_PRICE = BigInteger.valueOf(2_000_000_000L);

    private final Web3j web3j;
    private String contractAddress;

    private Credentials credentials;

    public PostEventService(Web3j web3j, ApplicationProperties applicationProperties) {
        this.web3j = web3j;
        this.contractAddress = applicationProperties.getContractAddress();
    }

    @PostConstruct
    public void listen() throws Exception {
        Credentials credentials = Credentials.create(PRIVATE_KEY);
        EthBoard ethBoard = EthBoard.load(contractAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        log.debug("contract address {}", ethBoard.getContractAddress());

        ethBoard.addedPostEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
        .subscribe(response -> {
            log.debug("ethLog {} {} {} {} {} {}", response.ownerAccount, response.postID,
             response.firstName, response.lastName, response.title, response.content);
        });

        log.info("Subscribed");
    }

    // @PostConstruct
    // public void init() throws IOException, CipherException, NoSuchAlgorithmException, NoSuchProviderException,
    //         InvalidAlgorithmParameterException {

    //     String file = WalletUtils.generateLightNewWalletFile("piot123", null);
    //     credentials = WalletUtils.loadCredentials("piot123", file);
    //     log.info("Credentials created: file={}, address={}", file, credentials.getAddress());
    //     EthCoinbase coinbase = web3j.ethCoinbase().send();
    //     EthGetTransactionCount transactionCount = web3j
    //             .ethGetTransactionCount(coinbase.getAddress(), DefaultBlockParameterName.LATEST).send();
    //     Transaction transaction = Transaction.createEtherTransaction(coinbase.getAddress(),
    //             transactionCount.getTransactionCount(), BigInteger.valueOf(20_000_000_000L), BigInteger.valueOf(21_000),
    //             credentials.getAddress(), BigInteger.valueOf(25_000_000_000_000_000L));
    //     web3j.ethSendTransaction(transaction).send();
    //     EthGetBalance balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
    //     log.info("Balance: {}", balance.getBalance().longValue());
    // }

}
