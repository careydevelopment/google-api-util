package us.careydevelopment.util.api.google;

import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStore;
import us.careydevelopment.api.google.datastore.GoogleDataStoreFactory;
import us.careydevelopment.util.api.google.config.GoogleApiConfig;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * This utility class streamlines the process of instantiating and using the
 * GoogleAuthorizationCodeFlow object.
 *
 * Calling clients only need a user ID to get the ball rolling. But they can also
 * provide a List of scopes if they want to specify scopes on the fly.
 */
public class AuthorizationCodeFlowUtil {

    /**
     * Gets the StoredCredentialDataStore object.
     *
     * As the name implies, the framework uses that object to retrieve and save the
     * StoredCredential object.
     *
     * @return the data store object
     * @throws IOException
     */
    private static DataStore<StoredCredential> getStoredCredentialDataStore() throws IOException {
        final GoogleDataStoreFactory factory = GoogleDataStoreFactory.getInstance();
        final DataStore<StoredCredential> dataStore = factory.getDataStore(GoogleDataStoreFactory.CREDENTIAL_STORE_ID);
        return dataStore;
    }

    /**
     * Gets the listeners that determine if the token needs a refresh.
     *
     * @param userId
     * @param dataStore
     * @return list of CredentialRefreshListener objects
     * @throws IOException
     */
    private static List<CredentialRefreshListener> getListeners(final String userId,
                                                                final DataStore<StoredCredential> dataStore) throws IOException {
        final DataStoreCredentialRefreshListener listener = new DataStoreCredentialRefreshListener(userId, dataStore);
        final List<CredentialRefreshListener> listeners = new ArrayList<>();
        listeners.add(listener);

        return listeners;
    }

    /**
     * Gets the GoogleAuthorizationCodeFlow object with just the user ID.
     *
     * Clients that use this method MUST have set the scopes in the GoogleApiConfig object. Otherwise, the
     * framework will throw an exception.
     *
     * @param userId
     * @return GoogleAuthorizationCodeFlow instance
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(final String userId) throws IOException, GeneralSecurityException {
        final GoogleApiConfig config = GoogleApiConfig.getInstance();
        final List<String> scopes = config.getScopes();

        return getAuthorizationCodeFlow(userId, scopes);
    }

    /**
     * This method takes both a user ID and a List of scopes and builds the
     * GoogleAuthorizationCodeFlow object accordingly.
     *
     * @param userId
     * @param scopes
     * @return GoogleAuthorizationCodeFlow object
     * @throws IOException
     * @throws GeneralSecurityException
     */
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
