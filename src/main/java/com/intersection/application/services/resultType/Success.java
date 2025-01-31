package com.intersection.application.services.resultType;

public class Success<T> implements IResultType<T> {
    private final T result;
    private final String message;

    public Success(String message, T result) {
        this.result = result;
        this.message = message;
    }

    public Success(String message) {
        this(message, null);
    }

    @Override
    public T getResult() {
        return result;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
