package io.blocko.ethsearch.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.blocko.ethsearch.extension.SpringExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Lazy
// @ComponentScan(basePackages = {"io.blocko.ethsearch.actors", "io.blocko.ethsearch.extension"})
public class EthsearchAkkaConfiguration {

    private final Logger log = LoggerFactory.getLogger(EthsearchAkkaConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    @Autowired
    private Environment env;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("EthsearchApp", akkaConfiguration());
        springExtension.initialize(applicationContext);
        return system;
    }

    @Bean
    public Config akkaConfiguration() {
        String akkaip = env.getProperty("akka.remote.netty.tcp.ip");
        String port = env.getProperty("akka.remote.netty.tcp.port");

        return ConfigFactory.load().withValue("akka.remote.netty.tcp.ip", ConfigValueFactory.fromAnyRef(akkaip))
        .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(port));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        log.debug("onApplicationReadyEvent");
        ActorSystem system = applicationContext.getBean(ActorSystem.class);
        final LoggingAdapter akkaLog = Logging.getLogger(system, "EthsearchApp");

        SpringExtension ext = applicationContext.getBean(SpringExtension.class);
        ActorRef ethActor = system.actorOf(ext.props("ethsearchActor"));
        ethActor.tell("ready spring actor", null);
    }
}
