package finalyearproject.controller;

import finalyearproject.model.Artist;
import finalyearproject.repository.ArtistRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArtistControllerTest {
    private Artist artist;

    @InjectMocks
    private ArtistController artistController;

    @Mock
    private ArtistRepository artistRepository;

    @Before
    public void init() throws MalformedURLException, URISyntaxException {
        MockitoAnnotations.initMocks(this);
        artist = new Artist(
                "5ivCbtrcD5N4rD337xIb2z",
                new URL("https://open.spotify.com/artist/5ivCbtrcD5N4rD337xIb2z"),
                new String[]{"indie pop", "indie poptimism", "indietronica", "modern rock", "pop"},
                "https://api.spotify.com/v1/artists/5ivCbtrcD5N4rD337xIb2z",
                "MisterWives",
                "artist",
                new URI("spotify:artist:5ivCbtrcD5N4rD337xIb2z"));
    }

    @Test
    public void artistGet() throws URISyntaxException, MalformedURLException {
        when(artistRepository.getOne("5ivCbtrcD5N4rD337xIb2z")).thenReturn(artist);

        Artist misterWives = artistController.get("5ivCbtrcD5N4rD337xIb2z");
        assertEquals("5ivCbtrcD5N4rD337xIb2z", misterWives.getId());
        assertEquals("MisterWives", misterWives.getName());

    }

    @Test
    public void shouldSaveAndFlushOnCreate() {
        artistController.create(artist);
        verify(artistRepository, times(1)).saveAndFlush(artist);
    }

}
