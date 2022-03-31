package us.careydevelopment.util.api.google.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.api.google.datastore.config.GoogleDataStoreConfig;
import us.careydevelopment.api.google.datastore.util.StoredCredentialPersister;
import us.careydevelopment.api.google.datastore.util.StoredCredentialRetriever;
import us.careydevelopment.util.api.google.exception.GoogleApiConfigException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * The configuration class for all API activities.
 *
 * THis class must be instantiated using the Builder. It's a singleton so only one object will
 * ever exist.
 *
 * It's a good idea to instantiate this class with some initialization code within your
 * application.
 */
public class GoogleApiConfig {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleApiConfig.class);

    private String clientId;
    private String clientSecret;
    private StoredCredentialRetriever retriever;
    private StoredCredentialPersister persister;
    private List<String> scopes;
    private HttpTransport transport;
    private JsonFactory jsonFactory;
    private String applicationName;

    private static GoogleApiConfig INSTANCE = null;

    /**
     * Private constructor to prevent outside instantiation.
     *
     * @param builder
     */
    private GoogleApiConfig(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.persister = builder.persister;
        this.retriever = builder.retriever;
        this.scopes = builder.scopes;
        this.applicationName = builder.applicationName;
        this.jsonFactory = builder.jsonFactory;
        this.transport = builder.transport;

        instantiateDependencies();

        INSTANCE = this;
    }

    private void instantiateDependencies() {
        instantiateDataStoreConfig();
    }

    /**
     * This data store holds the StoredCredential object that the
     * application will use to obtain access to various Google properties.
     */
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

    public HttpTransport getTransport() {
        return transport;
    }

    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Will only return the instance if the object has been created via the Builder.
     *
     * @return GoogleApiConfig instance
     */
    public static GoogleApiConfig getInstance() {
        if (INSTANCE == null) {
            throw new GoogleApiConfigException("GoogleApiConfig not built yet!");
        }

        return INSTANCE;
    }

    public static void shutdown() {
        INSTANCE = null;
    }

    /**
     * The Build class instantiates the GoogleApiConfig object.
     *
     * The setter methods all return an instance of this object so developers
     * can use method chaining to instantiate the config object.
     */
    public static class Builder {

        private String clientId;
        private String clientSecret;
        private StoredCredentialRetriever retriever;
        private StoredCredentialPersister persister;
        private List<String> scopes = new ArrayList<>();
        private HttpTransport transport;
        private JsonFactory jsonFactory = new GsonFactory();
        private String applicationName;

        public static Builder instance() {
            return new Builder();
        }

        private Builder() {
            setup();
        }

        private void setup() {
            try {
                this.transport = GoogleNetHttpTransport.newTrustedTransport();
            } catch (IOException ie) {
                LOG.error("IO problem when establishing transport!", ie);
                throw new GoogleApiConfigException(ie.getMessage());
            } catch (GeneralSecurityException ge) {
                LOG.error("Security issue when establishing transport!", ge);
                throw new GoogleApiConfigException(ge.getMessage());
            }
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

        public Builder setTransport(HttpTransport transport) {
            this.transport = transport;
            return this;
        }

        public Builder setJsonFactory(JsonFactory factory) {
            this.jsonFactory = factory;
            return this;
        }

        public Builder setApplicationName(String applicationName) {
            this.applicationName = applicationName;
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
