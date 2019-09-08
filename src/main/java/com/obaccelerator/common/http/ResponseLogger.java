package com.obaccelerator.common.http;

import org.apache.http.HttpResponse;

public interface ResponseLogger {
    String log(String rawResponse);
}
