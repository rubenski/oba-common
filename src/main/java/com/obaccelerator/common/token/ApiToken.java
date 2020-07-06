package com.obaccelerator.common.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiToken {
    private String apiToken;
    private long expires;
}
