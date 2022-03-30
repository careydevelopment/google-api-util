package us.careydevelopment.util.api.google;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.careydevelopment.util.api.google.exception.GoogleApiException;

public class AuthorizationCodeUrlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationCodeUrlUtil.class);

    public static String getAuthorizationCodeUrl(String id, String redirectUrl) {
        return getAuthorizationCodeUrl(id, redirectUrl, null);
    }

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
