package finalyearproject.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    String clientId;
    String clientSecret;

    @Autowired
    public AuthenticationService(@Value("${clientId}") String clientId, @Value("${clientSecret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Api clientCredentialflow() {
        final Api api = Api.builder()
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .build();

        log.info("ClientId {} clientSecret {}", clientId, clientSecret);

        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {

                log.info("Successfully retrieved an access token: " + clientCredentials.getAccessToken());
                log.info("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

                api.setAccessToken(clientCredentials.getAccessToken());

            }
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Could not retrieve an access token");
            }
        });
        return api;
    }
}
