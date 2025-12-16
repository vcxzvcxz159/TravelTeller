package com.c3.travleteller.config.oci;

import org.springframework.core.env.ConfigurableEnvironment;

public class OciVaultPropertiesBinder {

    public static OciVaultProperties bind(ConfigurableEnvironment environment) {
        OciVaultProperties props = new OciVaultProperties();

        props.setAuthType(environment.getProperty("oci.vault.auth-type", "file"));
        props.setConfigFile(environment.getProperty("oci.vault.config-file", System.getProperty("user.home") + "/.oci/config"));
        props.setConfigProfile(environment.getProperty("oci.vault.config-profile", "DEFAULT"));
        props.setSecretOcid(environment.getProperty("oci.vault.secret-ocid"));

        return props;
    }
}
