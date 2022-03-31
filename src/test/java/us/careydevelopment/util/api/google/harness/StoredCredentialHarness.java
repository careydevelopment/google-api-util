package us.careydevelopment.util.api.google.harness;

import com.google.api.client.auth.oauth2.StoredCredential;

public class StoredCredentialHarness {

    public static String ACCESS_TOKEN = "ey87hsdf";
    public static String REFRESH_TOKEN = "3/22uyr";

    public static StoredCredential getGoodStoredCredential() {
        StoredCredential credential = new StoredCredential();

        credential.setAccessToken(ACCESS_TOKEN);
        credential.setRefreshToken(REFRESH_TOKEN);

        return credential;
    }
}
