package io.blocko.ethsearch.actor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

@Component
@Scope("prototype")
public class EthsearchActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), "EthsearchActor");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(String.class, message -> {
            log.debug("Incoming message {}", message);
        }).build();
    }

}
