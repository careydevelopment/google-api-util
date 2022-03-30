package us.careydevelopment.util.api.google;

import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStore;
import us.careydevelopment.api.google.datastore.GoogleDataStoreFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationCodeFlowUtil {

    private static DataStore<StoredCredential> getStoredCredentialDataStore() throws IOException {
        final GoogleDataStoreFactory factory = GoogleDataStoreFactory.getInstance();
        final DataStore<StoredCredential> dataStore = factory.getDataStore(GoogleDataStoreFactory.CREDENTIAL_STORE_ID);
        return dataStore;
    }

    private static List<CredentialRefreshListener> getListeners(final String userId,
                                                                final DataStore<StoredCredential> dataStore) throws IOException {
        final DataStoreCredentialRefreshListener listener = new DataStoreCredentialRefreshListener(userId, dataStore);
        final List<CredentialRefreshListener> listeners = new ArrayList<>();
        listeners.add(listener);

        return listeners;
    }

    public static GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(final String userId) throws IOException, GeneralSecurityException {
        final GoogleApiConfig config = GoogleApiConfig.getInstance();
        final List<String> scopes = config.getScopes();

        return getAuthorizationCodeFlow(userId, scopes);
    }

    public static GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(final String userId, final List<String> scopes) throws IOException, GeneralSecurityException {
        final DataStore<StoredCredential> dataStore = getStoredCredentialDataStore();
        final List<CredentialRefreshListener> listeners = getListeners(userId, dataStore);
        final GoogleApiConfig config = GoogleApiConfig.getInstance();
        final String clientId = config.getClientId();
        final String clientSecret = config.getClientSecret();

        final GoogleAuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new GsonFactory(),
                clientId,
                clientSecret,
                scopes)
                .setCredentialDataStore(dataStore)
                .setAccessType("offline")
                .setRefreshListeners(listeners)
                .build();

        return authorizationCodeFlow;
    }
}
