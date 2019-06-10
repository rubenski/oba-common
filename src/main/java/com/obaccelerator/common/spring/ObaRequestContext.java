package com.obaccelerator.common.spring;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ObaRequestContext {
    private String clientId;
}
