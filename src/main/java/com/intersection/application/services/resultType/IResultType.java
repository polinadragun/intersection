package com.intersection.application.services.resultType;

public interface IResultType<T> {
    T getResult();

    String getMessage();
}
