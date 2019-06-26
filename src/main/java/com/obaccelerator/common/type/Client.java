package com.obaccelerator.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private UUID id;
    private String name;
    private String email;
    private boolean emailVerified;
    private LocalDateTime created;
}
