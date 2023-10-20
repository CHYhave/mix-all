package org.ajhy.notify;

/**
 * @author chy
 * @description
 * @date 2023/10/20
 */
public abstract class SlowEvent extends Event{
    @Override
    public long sequence() {
        return 0;
    }
}
