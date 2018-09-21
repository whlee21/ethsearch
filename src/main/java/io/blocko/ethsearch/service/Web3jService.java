package io.blocko.ethsearch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetStorageAt;

import rx.Subscription;

@Service
@Transactional
public class Web3jService {

    private final Logger log = LoggerFactory.getLogger(Web3jService.class);
    // private String CONTRACT_ADDRESS = "0xf1f5896ace3a78c347eb7eab503450bc93bd0c3b";
    private String CONTRACT_ADDRESS = "0xd2f97c8Abd47776dD74E230f9dD6c53298C2f5bB";

    @Autowired
    private Web3j web3;

    @Async("taskExecutor")
    public CompletableFuture<EthBlock> getLastBlockAsync() throws IOException {
        // EthBlock block =
        // web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.TEN),
        // false).send();
        CompletableFuture<EthBlock> block = web3.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).sendAsync();
        // BigInteger blockNumber = block.getBlock().getNumber();
        // log.info("blockNumber {}", blockNumber);

        return block;
    }

    // @Async("taskExecutor")
    public void getLastBlockObservable() throws InterruptedException {
         Subscription subscription = web3.blockObservable(true).subscribe(block -> {
             BigInteger blockNumber = block.getBlock().getNumber();
             log.info("blockNumber {}", blockNumber);
         }, Throwable::printStackTrace);
         log.info("unsubscribe");
         subscription.unsubscribe();
    }

    // @Async("taskExecutor")
    public void readStorage() throws IOException {
        for (int index = 0; index < 10; index++) {
            EthGetStorageAt ethStorage = web3.ethGetStorageAt(CONTRACT_ADDRESS, BigInteger.valueOf(index), DefaultBlockParameterName.EARLIEST).send();
            // log.debug("[{}] {}", index, web3.ethGetStorageAt(CONTRACT_ADDRESS, index, DefaultBlockParameterName.LATEST).send();
            log.debug("[{}] {} {}", index, ethStorage.getData(), ethStorage.getResult());
        }
    }

    public void readObservable() {
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, CONTRACT_ADDRESS);
        web3.ethLogObservable(filter).subscribe(ethLog -> {
            log.debug("{} {}", ethLog.getAddress(), ethLog.getData());
        });
    }
}
