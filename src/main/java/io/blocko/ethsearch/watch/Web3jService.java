package io.blocko.ethsearch.watch;

import java.io.IOException;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

@Service
public class Web3jService {

    private final Logger log = LoggerFactory.getLogger(Web3jService.class);

    @Autowired
    private Web3j web3;

    @Async("taskExecutor")
    public BigInteger getLastBlockAsync() throws IOException {
        //  Subscription subscription = web3.blockObservable(false).subscribe(block -> {
        //      BigInteger blockNumber = block.getBlock().getNumber();
        //      log.info("blockNumber {}", blockNumber);
        //  }, Throwable::printStackTrace);
        //  subscription.unsubscribe();
        EthBlock block = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO), false).send();
        BigInteger blockNumber = block.getBlock().getNumber();
        log.info("blockNumber {}", blockNumber);

        return blockNumber;
    }

}
