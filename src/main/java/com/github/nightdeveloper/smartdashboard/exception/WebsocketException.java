package com.github.nightdeveloper.smartdashboard.exception;

public class WebsocketException extends Exception {

    public WebsocketException(String message) {
        super(message);
    }

    public WebsocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
