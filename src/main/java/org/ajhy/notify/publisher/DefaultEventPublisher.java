package org.ajhy.notify.publisher;

import org.ajhy.notify.Event;
import org.ajhy.notify.subscriber.Subscriber;

import java.io.IOException;

/**
 * @author chy
 * @description
 * @date 2023/10/18
 */
public class DefaultEventPublisher implements EventPublisher {
    @Override
    public void init(Class<? extends Event> type, int bufferSize) {

    }

    @Override
    public long currentEventSize() {
        return 0;
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {

    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {

    }

    @Override
    public boolean publish(Event event) {
        return false;
    }

    @Override
    public void notifySubscriber(Subscriber subscriber, Event event) {

    }

    @Override
    public void shutdown() throws RuntimeException {

    }
}
