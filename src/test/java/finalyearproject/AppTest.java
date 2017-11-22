package finalyearproject;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Track;
import finalyearproject.controller.HomeController;
import finalyearproject.service.AuthenticationService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ComponentScan
public class AppTest {

    private final static Logger logger = Logger.getLogger(AppTest.class);
    @Value("${clientId}")
    String clientID;
    @Value("${clientSecret}")
    String clientSecret;
    @Value("${redirectURI}")
    String redirectURI;

    @Test
    public void testApp() {
        HomeController hc = new HomeController();
        String result = hc.home();
        assertEquals(result, "Welcome Home");

    }

    @Test
    public void connectionToSpotifyApi() {
        Api api = Api.builder()
                .clientId(clientID)
                .clientSecret(clientSecret)
                .redirectURI(redirectURI)
                .build();
        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {
                logger.info("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
            }

            @Override
            public void onFailure(Throwable throwable) {
                fail();
            }
        });

    }

    @Test
    public void retrieveSongFromApi() {
        AuthenticationService authenticationService = new AuthenticationService(clientID, clientSecret);
        Api api = authenticationService.clientCredentialflow();
        final TrackRequest request = api.getTrack("3fJaqjV813edLN5wrxUPkc").build();

        try {
            Track track = request.get();
            assertEquals("Verifying Song Name: ", track.getName(), "Strangers");
            assertEquals("Verifying Artist: ", track.getArtists().get(0).getName(), "Sigrid");
        } catch (WebApiException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Configuration
    @ComponentScan("finalyearproject")
    class Config {

        @Bean
        PropertyPlaceholderConfigurer propConfig() {
            PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
            placeholderConfigurer.setLocation(new ClassPathResource("application-test.properties"));
            return placeholderConfigurer;
        }

    }

}
