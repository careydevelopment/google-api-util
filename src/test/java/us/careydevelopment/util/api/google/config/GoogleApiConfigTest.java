package us.careydevelopment.util.api.google.config;

import com.google.api.services.gmail.GmailScopes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.careydevelopment.api.google.datastore.util.StoredCredentialPersister;
import us.careydevelopment.api.google.datastore.util.StoredCredentialRetriever;
import us.careydevelopment.util.api.google.exception.GoogleApiConfigException;
import us.careydevelopment.util.api.google.harness.StoredCredentialPersisterHarness;
import us.careydevelopment.util.api.google.harness.StoredCredentialRetrieverHarness;

import java.util.List;

public class GoogleApiConfigTest {

    private static final String CLIENT_ID = "309a9";
    private static final String CLIENT_SECRET = "0e84e";
    private static final List<String> SCOPES = List.of(GmailScopes.MAIL_GOOGLE_COM);

    @Test
    public void testGetInstanceWithoutBuilder() {
        GoogleApiConfig.shutdown();
        Assertions.assertThrows(GoogleApiConfigException.class, () -> GoogleApiConfig.getInstance());
    }

    @Test
    public void testBuildWithoutDependencies() {
        GoogleApiConfig.shutdown();

        GoogleApiConfig.Builder builder = GoogleApiConfig.Builder.instance();
        Assertions.assertThrows(GoogleApiConfigException.class, () -> builder.build());
    }

    @Test
    public void testGoodBuild() {
        GoogleApiConfig.shutdown();

        final StoredCredentialPersister goodPersister = StoredCredentialPersisterHarness.getGoodPersister();
        final StoredCredentialRetriever goodRetriever = StoredCredentialRetrieverHarness.getGoodRetriever();

        GoogleApiConfig config = GoogleApiConfig.Builder
                                        .instance()
                                        .setClientId(CLIENT_ID)
                                        .setClientSecret(CLIENT_SECRET)
                                        .setPersister(goodPersister)
                                        .setRetriever(goodRetriever)
                                        .setScopes(SCOPES)
                                        .build();

        Assertions.assertEquals(goodPersister, config.getPersister());
        Assertions.assertEquals(goodRetriever, config.getRetriever());
        Assertions.assertEquals(CLIENT_ID, config.getClientId());
        Assertions.assertEquals(CLIENT_SECRET, config.getClientSecret());
        Assertions.assertEquals(SCOPES, config.getScopes());
    }
}
