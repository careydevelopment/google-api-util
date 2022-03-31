package us.careydevelopment.util.api.google.harness;

import com.google.api.client.auth.oauth2.StoredCredential;
import us.careydevelopment.api.google.datastore.util.StoredCredentialRetriever;

import java.io.IOException;

public class StoredCredentialRetrieverHarness {
    public static final String ID = "77";

    public static StoredCredentialRetriever getGoodRetriever() {
        return new GoogleApiStoredCredentialRetriever();
    }

    private static class GoogleApiStoredCredentialRetriever implements StoredCredentialRetriever {

        @Override
        public StoredCredential retrieve(String id) throws IOException {
            if (id != null && id.equals(ID)) {
                return StoredCredentialHarness.getGoodStoredCredential();
            }

            return null;
        }
    }
}
