package finalyearproject;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.ArtistRequest;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.*;
import finalyearproject.controller.HomeController;
import finalyearproject.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@ComponentScan
@Slf4j
public class AppTest {

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
                log.info("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
            }

            @Override
            public void onFailure(Throwable throwable) {
                fail();
            }
        });

    }

    //Tests to check health of important API endpoints, Should be run especially when 404 errors are returned in the logs.
    // Example: https://github.com/spotify/web-api/issues/733

    @Test
    public void retrieveSongFromApi() {
        AuthenticationService authenticationService = new AuthenticationService(clientID, clientSecret);
        Api api = authenticationService.clientCredentialflow();
        final TrackRequest request = api.getTrack("3fJaqjV813edLN5wrxUPkc").build();

        try {
            Track track = request.get();
            assertEquals("Verifying Song Name: ", "Strangers", track.getName());
            assertEquals("Verifying Artist: ", "Sigrid", track.getArtists().get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void retrievePlaylistFromApi() {
        AuthenticationService authenticationService = new AuthenticationService(clientID, clientSecret);
        Api api = authenticationService.clientCredentialflow();
        final PlaylistRequest request = api.getPlaylist("spotifycharts", "37i9dQZEVXbMDoHDwVN2tF").build();
        try {
            final Playlist playlist = request.get();
            assertEquals("Verifying Playlist Name: ", "Global Top 50", playlist.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void retrieveAlbumFromApi() {
        AuthenticationService authenticationService = new AuthenticationService(clientID, clientSecret);
        Api api = authenticationService.clientCredentialflow();
        final AlbumRequest request = api.getAlbum("0HEKWtu7St3tKgZDKZsX90").build();

        try {
            final Album album = request.get();
            assertEquals("Verifying Album Name: ", "Youthquake", album.getName());
            assertEquals("Verifying Artist Name: ", "Dead Or Alive", album.getArtists().get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void retrieveArtistFromApi() {
        AuthenticationService authenticationService = new AuthenticationService(clientID, clientSecret);
        Api api = authenticationService.clientCredentialflow();

        final ArtistRequest request = api.getArtist("6hN9F0iuULZYWXppob22Aj").build();

        try {
            final Artist artist = request.get();
            assertEquals("Verifying Artist Name: ", "Simple Minds", artist.getName());
            assertEquals("Verifying Artist Genre:", "art rock", artist.getGenres().get(0));

        } catch (Exception e) {
            System.out.println("Something went wrong!" + e.getMessage());
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
