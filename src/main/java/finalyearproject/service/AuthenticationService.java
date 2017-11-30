package finalyearproject.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



@Configuration
@Component
@NoArgsConstructor
@Data
public class AuthenticationService {
    @Value("${clientId}")
    private String clientId;
    @Value("${clientSecret}")
    private String clientSecret;

    private final static Logger logger = Logger.getLogger(AuthenticationService.class);

    public AuthenticationService(@Value("${clientId}") String clientId, @Value("${clientSecret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Api clientCredentialflow() {
        final Api api = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {

                logger.info("Successfully retrieved an access token: " + clientCredentials.getAccessToken());
                logger.info("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

                api.setAccessToken(clientCredentials.getAccessToken());


            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.error("Could not retrieve an access token");
            }
        });
        return api;
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreUnresolvablePlaceholders(true);
        c.setLocation(new ClassPathResource("application.properties"));
        return c;
    }
}
