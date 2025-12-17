package com.c3.travleteller.config;

import com.c3.travleteller.config.oci.OciVaultProperties;
import com.c3.travleteller.config.oci.OciVaultPropertiesBinder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class OciVaultApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    static {
        System.out.println(">>> OciVaultEnvironmentPostProcessor loaded <<<");
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println(">>> OCI Vault 조회 시작 <<<");

        OciVaultProperties ociVaultProperties = OciVaultPropertiesBinder.bind(environment);

        try {
            AuthenticationDetailsProvider provider =  new ConfigFileAuthenticationDetailsProvider(
                    ociVaultProperties.getConfigFile(),
                    ociVaultProperties.getConfigProfile());

            String jsonSecret = fetchSecretFromVault(provider, ociVaultProperties);

            Map<String, Object> vaultSecrets = objectMapper.readValue(
                    jsonSecret, new TypeReference<>() {}
            );

            environment.getPropertySources().addFirst(new MapPropertySource("ociVaultSecrets", vaultSecrets));
            System.out.println(">>> Vault Secret 환경에 등록 완료 <<<");

        } catch (Exception e) {
            throw new IllegalStateException("OCI Vault Secret 로딩 실패", e); 
        }
    }

    /**
     * OCI Vault에서 Secret OCID에 해당하는 Base64 인코딩된 Secret 값을 가져와 디코딩합니다.
     */
    private String fetchSecretFromVault(AuthenticationDetailsProvider provider, OciVaultProperties ociVaultProperties) {

        try (SecretsClient secretsClient = SecretsClient.builder().build(provider)) {
            log.info("🔐 OCI Vault Secret 조회 시작. Secret OCID: {}", ociVaultProperties.getSecretOcid());

            GetSecretBundleRequest request = GetSecretBundleRequest.builder()
                    .secretId(ociVaultProperties.getSecretOcid())
                    .build();

            GetSecretBundleResponse response = secretsClient.getSecretBundle(request);
            Base64SecretBundleContentDetails content =
                    (Base64SecretBundleContentDetails) response.getSecretBundle().getSecretBundleContent();

            return new String(Base64.getDecoder().decode(content.getContent()));
        }
    }
}
