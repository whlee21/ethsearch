package io.blocko.ethsearch.extension;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import akka.actor.Extension;
import akka.actor.Props;

@Component
public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

        /**
     * Used to initialize the Spring application context for the extension.
     */
    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Create a Props for the specified actorBeanName using the
     * SpringActorProducer class.
     */
    public Props props(String actorBeanName, Object... args) {
        return (args != null && args.length > 0) ?
             Props.create(SpringActorProducer.class,
                     applicationContext,
                     actorBeanName, args) :
             Props.create(SpringActorProducer.class,
                     applicationContext,
                     actorBeanName);
     }
}
