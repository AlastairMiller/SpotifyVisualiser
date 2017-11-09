package finalyearproject.controller;

import finalyearproject.model.Playlist;
import finalyearproject.repository.PlaylistRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaylistControllerTest {
    private Playlist playlist;

    @InjectMocks
    private PlaylistController playlistController;

    @Mock
    private PlaylistRepository playlistRepository;

    @Before
    public void init() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);
        playlist = new Playlist(
                "37i9dQZF1DX4W3aJJYCDfV",
                new URI("spotify:user:spotify:playlist:37i9dQZF1DX4W3aJJYCDfV"),
                null,
                520418,
                "https://api.spotify.com/v1/users/spotify/playlists/37i9dQZF1DX4W3aJJYCDfV",
                null,
                "New Music Friday UK",
                null,
                true,
                "xwO/oZuuxK0YCTkJdHGSEWuDySCrI0dVfKp4uBzsUqkiiXMYMM6ddK7923h58BwFyopvJ7S32xg=",
                null
        );
    }

    @Test
    public void playlistGet() {
        when(playlistRepository.getOne("37i9dQZF1DX4W3aJJYCDfV")).thenReturn(playlist);

        Playlist newMusicFriday = playlistController.get("37i9dQZF1DX4W3aJJYCDfV");
        assertEquals("37i9dQZF1DX4W3aJJYCDfV", newMusicFriday.getId());
        assertEquals("New Music Friday UK", newMusicFriday.getName());
    }

    @Test
    public void shouldSaveAndFlushOnCreate() {
        playlistController.create(playlist);
        verify(playlistRepository, times(1)).saveAndFlush(playlist);

    }
}
