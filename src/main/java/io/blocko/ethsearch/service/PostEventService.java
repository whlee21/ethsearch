package io.blocko.ethsearch.service;

import static io.blocko.ethsearch.config.Constants.GAS_LIMIT;
import static io.blocko.ethsearch.config.Constants.GAS_PRICE;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import io.blocko.ethsearch.config.ApplicationProperties;
import io.blocko.ethsearch.contract.EthBoard;
import io.blocko.ethsearch.contract.EthBoard.AddedPostEventResponse;
import io.blocko.ethsearch.domain.Post;
import io.blocko.ethsearch.repository.search.PostSearchRepository;

@Service
@Transactional
public class PostEventService {

    private final Logger log = LoggerFactory.getLogger(PostEventService.class);

    private String PRIVATE_KEY = "0x81bd7f6498d0642e5900b9cde860180aabe4b5d275617a15dace4ccc05a18207";
    // private String PRIVATE_KEY = "742a02a084659d55ac8e4c26ffa6fa9161712d5b1af1f864e145c937651c767c";

    private final Web3j web3j;
    private String contractAddress;
    private PostSearchRepository repository;

    private Credentials credentials;

    public PostEventService(Web3j web3j, PostSearchRepository repository, ApplicationProperties applicationProperties) {
        this.web3j = web3j;
        this.repository = repository;
        this.contractAddress = applicationProperties.getContractAddress();
    }

    @PostConstruct
    public void listen() throws Exception {
        Credentials credentials = Credentials.create(PRIVATE_KEY);
        EthBoard ethBoard = EthBoard.load(contractAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        log.debug("contract address {}", ethBoard.getContractAddress());

        ethBoard.addedPostEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    log.debug("AddedPostEvent {} {} {} {} {} {}", event.ownerAccount, event.postID, event.firstName,
                            event.lastName, event.title, event.content);
                    save(event);
                });

        log.info("Subscribed");
    }

    @Async
    public void save(AddedPostEventResponse event) {
        Post post = new Post();
        post.setId(event.postID.longValue());
        post.setOwnerAccount(event.ownerAccount);
        post.setOwnerId(event.ownerId);
        post.setOwnerFirstName(event.firstName);
        post.setOwnerLastName(event.lastName);
        post.setTitle(event.title);
        post.setContent(event.content);
        repository.save(post);
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
