package com.obaccelerator.common;

public interface Converter<I,O> {

    O convert(I input);
}
