package com.obaccelerator.common.type;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class Customer {
    private int id;
    private String name;
    private String email;
    private OffsetDateTime created;
}
