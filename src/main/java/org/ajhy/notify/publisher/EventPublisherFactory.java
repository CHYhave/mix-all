package org.ajhy.notify.publisher;

import org.ajhy.notify.Event;
import org.ajhy.notify.publisher.EventPublisher;

import java.util.function.BiFunction;

/**
 * @author chy
 * @description
 * @date 2023/10/18
 */
public interface EventPublisherFactory extends BiFunction<Class<? extends Event>, Integer, EventPublisher> {
    
    /**
     * Build an new {@link EventPublisher}.
     *
     * @param eventType    eventType for {@link EventPublisher}
     * @param maxQueueSize max queue size for {@link EventPublisher}
     * @return new {@link EventPublisher}
     */
    @Override
    EventPublisher apply(Class<? extends Event> eventType, Integer maxQueueSize);
}
