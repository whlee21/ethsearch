package io.blocko.ethsearch;

import io.blocko.ethsearch.config.ApplicationProperties;
import io.blocko.ethsearch.config.DefaultProfileUtil;
import io.blocko.ethsearch.extension.SpringExtension;
import io.github.jhipster.config.JHipsterConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class EthsearchApp {

    private static final Logger log = LoggerFactory.getLogger(EthsearchApp.class);

    private final Environment env;

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

        ActorSystem system = appContext.getBean(ActorSystem.class);
        final LoggingAdapter akkaLog = Logging.getLogger(system, "EthsearchApp");

        SpringExtension ext = appContext.getBean(SpringExtension.class);
        ActorRef ethActor = system.actorOf(ext.props("EthsearchActor"));
        ethActor.tell("ready spring actor", null);
    }

}
