package net.bindernews.eventbus;

public class EventBusException extends RuntimeException {
    private static final long serialVersionUID = 6336068849858095644L;

    public EventBusException(String message) {
        super(message);
    }

    public EventBusException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventBusException(Throwable cause) {
        super(cause);
    }
}
