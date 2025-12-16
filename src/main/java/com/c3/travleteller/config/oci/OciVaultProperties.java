package com.c3.travleteller.config.oci;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oci.vault")
public class OciVaultProperties {

    private String secretOcid;
    private String authType;
    private String configFile = "~/.oci/config";;
    private String configProfile = "DEFAULT";
}