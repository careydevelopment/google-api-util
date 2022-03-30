package us.careydevelopment.util.api.google;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.api.google.datastore.config.GoogleDataStoreConfig;
import us.careydevelopment.api.google.datastore.util.StoredCredentialPersister;
import us.careydevelopment.api.google.datastore.util.StoredCredentialRetriever;
import us.careydevelopment.util.api.google.exception.GoogleApiConfigException;

import java.util.ArrayList;
import java.util.List;

public class GoogleApiConfig {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleApiConfig.class);

    private String clientId;
    private String clientSecret;
    private StoredCredentialRetriever retriever;
    private StoredCredentialPersister persister;
    private List<String> scopes;

    private static GoogleApiConfig INSTANCE = null;

    private GoogleApiConfig(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.persister = builder.persister;
        this.retriever = builder.retriever;
        this.scopes = builder.scopes;

        instantiateDependencies();

        INSTANCE = this;
    }

    private void instantiateDependencies() {
        instantiateDataStoreConfig();
    }

    private void instantiateDataStoreConfig() {
        GoogleDataStoreConfig.Builder
                .instance()
                .setStoredCredentialPersister(persister)
                .setStoredCredentialRetriever(retriever)
                .build();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public StoredCredentialRetriever getRetriever() {
        return retriever;
    }

    public StoredCredentialPersister getPersister() {
        return persister;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public static GoogleApiConfig getInstance() {
        if (INSTANCE == null) {
            throw new GoogleApiConfigException("GoogleApiConfig not built yet!");
        }

        return INSTANCE;
    }

    public static class Builder {

        private String clientId;
        private String clientSecret;
        private StoredCredentialRetriever retriever;
        private StoredCredentialPersister persister;
        private List<String> scopes = new ArrayList<>();

        public static Builder instance() {
            return new Builder();
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder setRetriever(StoredCredentialRetriever retriever) {
            this.retriever = retriever;
            return this;
        }

        public Builder setPersister(StoredCredentialPersister persister) {
            this.persister = persister;
            return this;
        }

        public Builder setScopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public GoogleApiConfig build() {
            if (INSTANCE == null) {
                validate();
                return new GoogleApiConfig(this);
            } else {
                LOG.warn("GoogleApiConfig already exists - returning existing instance.");
                return INSTANCE;
            }
        }

        private void validate() {
            if (StringUtils.isBlank(clientId)) {
                throw new GoogleApiConfigException("Client ID is null!");
            }

            if (StringUtils.isBlank(clientSecret)) {
                throw new GoogleApiConfigException("Client Secret is null!");
            }

            if (persister == null) {
                throw new GoogleApiConfigException("Stored credential persister is null!");
            }

            if (retriever == null) {
                throw new GoogleApiConfigException("Store credential retriever is null!");
            }
        }
    }
}
