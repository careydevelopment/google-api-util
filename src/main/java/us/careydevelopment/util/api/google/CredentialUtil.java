package us.careydevelopment.util.api.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.util.api.google.exception.GoogleApiException;
import us.careydevelopment.util.api.google.model.GoogleAuthResponse;

/**
 * This utility class makes it easy to obtain the Credential object.
 * Clients will need provide an ID. That's typically the User ID on the system.
 *
 * Then, the framework will use the DataStore object to retrieve the persisted
 * StoredCredential.
 *
 * Google's code translates the StoredCredential to a Credential object.
 */
public class CredentialUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CredentialUtil.class);

    /**
     * Gets the Credential associated with the ID.
     *
     * @param id
     * @return Credential object
     */
    public static Credential getCredential(String id) {
        Credential credential = null;

        try {
            GoogleAuthorizationCodeFlow acf = AuthorizationCodeFlowUtil.getAuthorizationCodeFlow(id);
            credential = acf.loadCredential(id);
        } catch (Exception e) {
            LOG.error("Problem retrieving credential!", e);
            throw new GoogleApiException(e.getMessage());
        }

        return credential;
    }

    /**
     * This method gets the Credential from the authorization code.
     *
     * The GoogleAuthResponse object includes both the authorization code and
     * a redirect URL.
     *
     * This is the second step in an OAuth2 flow.
     *
     * @param auth
     * @param id
     * @return Credential object
     */
    public static Credential getCredentialFromCode(GoogleAuthResponse auth, String id) {
        Credential credential = null;

        try {
            final GoogleAuthorizationCodeFlow acf = AuthorizationCodeFlowUtil.getAuthorizationCodeFlow(id);

            LOG.debug("code is " + auth.getCode());

            final AuthorizationCodeTokenRequest req = acf.newTokenRequest(auth.getCode());
            req.setRedirectUri(auth.getRedirectUrl());

            final TokenResponse response = req.execute();
            LOG.debug(response.toPrettyString());

            credential = acf.createAndStoreCredential(response, id);
        } catch (Exception e ) {
            LOG.error("Problem creating credential!", e);
            throw new GoogleApiException(e.getMessage());
        }

        return credential;
    }
}
