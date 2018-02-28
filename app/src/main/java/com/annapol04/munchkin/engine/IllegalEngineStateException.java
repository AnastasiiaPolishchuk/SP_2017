package com.annapol04.munchkin.engine;

public class IllegalEngineStateException extends Exception {

    public IllegalEngineStateException() {
        super();
    }

    public IllegalEngineStateException(String message) {
        super(message);
    }

    public IllegalEngineStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalEngineStateException(Throwable cause) {
        super(cause);
    }
}
