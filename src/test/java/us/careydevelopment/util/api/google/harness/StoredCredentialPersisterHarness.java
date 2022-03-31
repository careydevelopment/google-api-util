package us.careydevelopment.util.api.google.harness;

import com.google.api.client.auth.oauth2.StoredCredential;
import us.careydevelopment.api.google.datastore.util.StoredCredentialPersister;

import java.io.IOException;

public class StoredCredentialPersisterHarness {

    public static String ID = StoredCredentialRetrieverHarness.ID;

    public static StoredCredentialPersister getGoodPersister() {
        return new GoogleApiStoredCredentialPersister();
    }

    private static class GoogleApiStoredCredentialPersister implements StoredCredentialPersister {

        @Override
        public boolean persist(String id, StoredCredential storedCredential) throws IOException {
            return false;
        }
    }
}
