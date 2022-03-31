package us.careydevelopment.util.api.google.harness;

import us.careydevelopment.api.google.datastore.config.GoogleDataStoreConfig;
import us.careydevelopment.api.google.datastore.util.StoredCredentialPersister;
import us.careydevelopment.api.google.datastore.util.StoredCredentialRetriever;

public class GoogleDataStoreConfigHarness {

    private static final StoredCredentialPersister PERSISTER = StoredCredentialPersisterHarness.getGoodPersister();
    private static final StoredCredentialRetriever RETRIEVER = StoredCredentialRetrieverHarness.getGoodRetriever();

    public static void setUpConfig() {
        GoogleDataStoreConfig.Builder
                .instance()
                .setStoredCredentialPersister(PERSISTER)
                .setStoredCredentialRetriever(RETRIEVER)
                .build();
    }
}
