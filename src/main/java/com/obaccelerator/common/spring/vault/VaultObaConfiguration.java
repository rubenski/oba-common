package com.obaccelerator.common.spring.vault;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("oba")
public class VaultObaConfiguration {

    @Value("${oba.db.user}")
    private String mysqlUser;
    @Value("${oba.db.password}")
    private String mysqlPassword;
    @Value("${oba.db.name}")
    private String dbName;
}
