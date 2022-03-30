package us.careydevelopment.util.api.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.util.api.google.exception.GoogleApiException;
import us.careydevelopment.util.api.google.model.GoogleAuthResponse;

public class CredentialUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CredentialUtil.class);

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
