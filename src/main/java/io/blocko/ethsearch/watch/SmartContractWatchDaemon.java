package io.blocko.ethsearch.watch;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

// @Component
public class SmartContractWatchDaemon {

    private final Logger log = LoggerFactory.getLogger(SmartContractWatchDaemon.class);
    private ExecutorService executorService;
    private volatile boolean stopThread = false;

    @Autowired
    private Web3jService web3jService;

    @PostConstruct
    public void init() {
        BasicThreadFactory factory  = new BasicThreadFactory.Builder()
        .namingPattern("smartcontractwatch-thread-%d").build();
        executorService = Executors.newSingleThreadExecutor(factory);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("thread started");
                    scanBlocks();
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }
        });
        executorService.shutdown();
    }

    private void scanBlocks() {
        while(!stopThread) {
            try {
                // CompletableFuture<EthBlock> block = web3jService.getLastBlockAsync();
                // CompletableFuture.allOf(block).join();
                // EthBlock ethBlock = block.get();
                // log.debug("{}", ethBlock.getBlock().getNumber());
                // for (TransactionResult tnx : ethBlock.getBlock().getTransactions()) {
                //     log.debug("{}", ((TransactionObject)tnx.get()).getInput());
                // }
                // web3jService.readStorage();
                // web3jService.readObservable();
                web3jService.getLastBlockObservable();

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                stopThread = true;
            }
        }
    }

    @PreDestroy
    public void destroy() {
        stopThread = true;
        if (executorService != null) {
            try {
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
