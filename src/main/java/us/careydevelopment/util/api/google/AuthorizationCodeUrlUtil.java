package us.careydevelopment.util.api.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.util.api.google.exception.GoogleApiException;

/**
 * This utility class makes it easy to get the authorization code flow URL.
 *
 * This is the first step in an OAuth2 transaction.
 */
public class AuthorizationCodeUrlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationCodeUrlUtil.class);

    /**
     * Gets the authorization code URL with no state.
     * State isn't required, so this method will be fine for most clients.
     *
     * @param id
     * @param redirectUrl
     * @return authorization code URL
     */
    public static String getAuthorizationCodeUrl(String id, String redirectUrl) {
        return getAuthorizationCodeUrl(id, redirectUrl, null);
    }

    /**
     * This method gets the authorization code URL with the given state.
     *
     * @param id
     * @param redirectUrl
     * @param state
     * @return authorization code URL
     */
    public static String getAuthorizationCodeUrl(String id, String redirectUrl, String state) {
        String url = null;

        try {
            url = AuthorizationCodeFlowUtil
                    .getAuthorizationCodeFlow(id)
                    .newAuthorizationUrl()
                    .setRedirectUri(redirectUrl)
                    .setState(state)
                    .build();

            LOG.debug("Authorization code URL is " + url);
        } catch (Exception e) {
            LOG.error("Problem getting authorization code!", e);
            throw new GoogleApiException(e.getMessage());
        }

        return url;
    }
}
