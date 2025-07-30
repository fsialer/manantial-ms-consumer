package com.fernando.manantial_ms_consumer.infrastructure.adapter.output.file;

public interface GenerateField<T> {
    byte[] generate(T data);
}
