package org.ajhy.notify;

/**
 * An interface is used to define the resource's close and shutdown, such as IO Connection and ThreadPool.
 *
 * @author zongtanghu
 */
public interface Closeable {
    
    /**
     * Shutdown the Resources, such as Thread Pool.
     *
     * @throws RuntimeException exception.
     */
    void shutdown() throws RuntimeException;
    
}
