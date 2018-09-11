package io.blocko.ethsearch.actors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

@Component
@Scope("prototype")
public class EthsearchActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), "EthsearchActor");

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof String) {
            log.info("Incoming message {}", message);
        } else {
            log.info("Unhandled message {}", message);
        }
    }
}
