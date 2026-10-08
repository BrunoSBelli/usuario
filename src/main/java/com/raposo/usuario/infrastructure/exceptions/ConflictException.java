package com.bruno.aprendendo_spring.infrastructure.exceptions;

import org.springframework.cache.interceptor.CacheOperationInvoker;

public class ConflictException extends RuntimeException{

    public ConflictException(String mensagem){
        super(mensagem);
    }

    public ConflictException(String mensagem, Throwable throwable){
        super(mensagem);
    }
}
