package org.ajhy.notify.subscriber;

import org.ajhy.notify.Event;

import java.util.concurrent.Executor;

/**
 * @author chy
 * @description
 * @date 2023/10/18
 */
public abstract class Subscriber<T extends Event> {
    /**
     * Event callback.
     *
     * @param event {@link Event}
     */
    public abstract void onEvent(T event);

    /**
     * Type of this subscriber's subscription.
     *
     * @return Class which extends {@link Event}
     */
    public abstract Class<? extends Event> subscribeType();

    /**
     * It is up to the listener to determine whether the callback is asynchronous or synchronous.
     *
     * @return {@link Executor}
     */
    public Executor executor() {
        return null;
    }

    /**
     * Whether to ignore expired events.
     *
     * @return default value is {@link Boolean#FALSE}
     */
    public boolean ignoreExpireEvent() {
        return false;
    }

    /**
     * Whether the event's scope matches current subscriber. Default implementation is all scopes matched.
     * If you override this method, it better to override related {@link com.alibaba.nacos.common.notify.Event#scope()}.
     *
     * @param event {@link Event}
     * @return Whether the event's scope matches current subscriber
     */
    public boolean scopeMatches(T event) {
        return true;
    }
}
