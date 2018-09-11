package io.blocko.ethsearch.extension;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

public class SpringActorProducer implements IndirectActorProducer {

    private final ApplicationContext applicationContext;
    private final String actorBeanName;
    private final Object[] args;

    public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.args = null;
    }

    public SpringActorProducer(ApplicationContext applicationContext,
     String actorBeanName, Object... args) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.args = args;
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }

    @Override
    public Actor produce() {
        return args == null ?
        (Actor) applicationContext.getBean(actorBeanName) :
        (Actor) applicationContext.getBean(actorBeanName, args);
	}

}
