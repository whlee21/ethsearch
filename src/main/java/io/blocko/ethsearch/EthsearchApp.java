package io.blocko.ethsearch;

import io.blocko.ethsearch.config.ApplicationProperties;
import io.blocko.ethsearch.config.DefaultProfileUtil;
import io.blocko.ethsearch.extension.SpringExtension;
import io.github.jhipster.config.JHipsterConstants;
import rx.Observable;
import rx.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.crypto.Credentials;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.blocko.ethsearch.contracts.EthBoard;
import static io.blocko.ethsearch.contracts.EthBoard.ADDEDPOST_EVENT;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
@ServletComponentScan
public class EthsearchApp {

    private static final Logger log = LoggerFactory.getLogger(EthsearchApp.class);

    private String CONTRACT_ADDRESS = "19023a1DD51e44A53221e6ea0a4722c6763974C2";
    private String PRIVATE_KEY = "81bd7f6498d0642e5900b9cde860180aabe4b5d275617a15dace4ccc05a18207";
    private BigInteger GAS_LIMIT = BigInteger.valueOf(80000000L);
    private BigInteger GAS_PRICE = BigInteger.valueOf(2000000000L);

    // "0x19023a1dd51e44a53221e6ea0a4722c6763974c2";
    private final Environment env;

    @Autowired
    private Web3j web3j;

    private Subscription txSubscription;

    public EthsearchApp(Environment env) {
        this.env = env;
    }


    /**
     * Initializes ethsearch.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EthsearchApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        // Environment env = app.run(args).getEnvironment();
        ApplicationContext appContext = app.run(args);
        Environment env = appContext.getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}\n\t" +
                "External: \t{}://{}:{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            hostAddress,
            env.getProperty("server.port"),
            env.getActiveProfiles());
    }


    @PostConstruct
    public void listen() throws Exception {

        // web3j.transactionObservable().subscribe(tx -> {

            // log.info("New tx: id={}, block={}, from={}, to={}, value={}", tx.getHash(), tx.getBlockHash(), tx.getFrom(), tx.getTo(), tx.getValue().intValue());

            // try {

                // EthCoinbase coinbase = web3j.ethCoinbase().send();
                // EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(tx.getFrom(), DefaultBlockParameterName.LATEST).send();
                // log.info("Tx count: {}", transactionCount.getTransactionCount().intValue());

                // if (transactionCount.getTransactionCount().intValue() % 10 == 0) {

                //     EthGetTransactionCount tc = web3j.ethGetTransactionCount(coinbase.getAddress(), DefaultBlockParameterName.LATEST).send();
                //     Transaction transaction = Transaction.createEtherTransaction(coinbase.getAddress(), tc.getTransactionCount(), tx.getValue(), BigInteger.valueOf(21_000), tx.getFrom(), tx.getValue());
                //     web3j.ethSendTransaction(transaction).send();
                // }

            // } catch (IOException e) {
            //     log.error("Error getting transactions", e);
            // }

        // });

        // web3j.ethGetLogs(ethFilter)

        // String encodedEventSignature = EventEncoder.encode(ADDEDPOST_EVENT);

        // EthFilter ethFilter = createFilterForEvent(encodedEventSignature, CONTRACT_ADDRESS);

        // web3j.ethLogObservable(ethFilter).subscribe(ethLog -> {
        //         log.debug("ethLog {}", ethLog);
        //     }
        // );

        Credentials credentials = Credentials.create(PRIVATE_KEY);
        EthBoard ethBoard = EthBoard.load(CONTRACT_ADDRESS, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        ethBoard.addedPostEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
        .subscribe(l -> {
            log.debug("ethLog {} {} {} {} {}", l.account, l.ownerName, l.postID, l.title, l.content);
        });

        // List<EthLog.LogResult> filterLogs = createFilterForEvent1(encodedEventSignature, CONTRACT_ADDRESS);

        log.info("Subscribed");
    }

    @PreDestroy
    public void unsubscribe() {
        // if (txSubscription != null) {
        //     txSubscription.unsubscribe();
        // }
    }

    private EthFilter createFilterForEvent(
        String encodedEventSignature, String contractAddress) {
        EthFilter ethFilter = new EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            contractAddress
        );
        ethFilter.addSingleTopic(encodedEventSignature);
        return ethFilter;
    }

    private List<EthLog.LogResult> createFilterForEvent1(
        String encodedEventSignature, String contractAddress) throws Exception {
        EthFilter ethFilter = new EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            contractAddress
        );
        ethFilter.addSingleTopic(encodedEventSignature);
        EthLog ethLog = web3j.ethGetLogs(ethFilter).send();
        return ethLog.getLogs();
    }

}
