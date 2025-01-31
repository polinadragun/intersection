package com.intersection.application.services.resultType;

public class Failure<T> implements IResultType<T> {
    private final T result;
    private final String message;

    public Failure(String message) {
        this.result = null;
        this.message = message;
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
