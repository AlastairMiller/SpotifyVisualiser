package finalyearproject;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.Track;
import finalyearproject.model.RefinedTrack;
import finalyearproject.repository.TrackRepository;
import finalyearproject.service.AuthenticationService;
import finalyearproject.service.RefreshLocalDbService;
import finalyearproject.utilities.DownstreamMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DataJpaTest
public class RefreshE2E {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    TrackRepository trackRepository;


    @Test
    public void pullSongAndStoreInDB() {
        Api api = authenticationService.clientCredentialFlow();
        Track track = RefreshLocalDbService.pullSong(api, "3fJaqjV813edLN5wrxUPkc");
        assert track != null;
        RefinedTrack refinedTrack = DownstreamMapper.mapSong(track);
        trackRepository.saveAndFlush(refinedTrack);
        RefinedTrack newRefinedTrack = trackRepository.findById(refinedTrack.getId());
        assertEquals(refinedTrack, newRefinedTrack);

    }

    @After
    public void cleanup() {
        trackRepository.deleteAll();
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
