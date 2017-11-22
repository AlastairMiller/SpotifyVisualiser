package finalyearproject;

import com.wrapper.spotify.models.*;
import finalyearproject.utilities.ImageBuilder;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class MapperTest {

    Track strangers = new Track();
    SimpleArtist simpleSigrid = new SimpleArtist();
    Artist fullSigrid = new Artist();


    @Before
    public void before() {

        Album strangersAlbum = new Album();
        strangersAlbum.setAlbumType(new AlbumType("album"));
        strangersAlbum.setExternalUrls(new ExternalUrls());
        strangersAlbum.setHref("https://api.spotify.com/v1/albums/2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setId("2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setImages(new ArrayList<Image>(Arrays.asList(
                ImageBuilder.getNewImage(640,640, "https://i.scdn.co/image/0725788b0ec7a6b2e35df5816780293f13acebef"),
                ImageBuilder.getNewImage(300,300, "https://i.scdn.co/image/7ad46eb909c9d472e8f9c02be988b4c00e2d2f9b"),
                ImageBuilder.getNewImage(64,64, "https://i.scdn.co/image/226910565e05223d962953b224f0d44133b978d0")))
        );
        strangersAlbum.setType();
        strangersAlbum.setUri("spotify:album:2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setAvailableMarkets();

        strangers.setAlbum();
        strangers.setArtists();
        strangers.setAvailableMarkets();
        strangers.setDiscNumber(1);
        strangers.setDuration(233725);
        strangers.setExplicit(false);
        strangers.setExternalIds();
        strangers.setExternalUrls();
        strangers.setHref("https://api.spotify.com/v1/tracks/3fJaqjV813edLN5wrxUPkc");
        strangers.setId("3fJaqjV813edLN5wrxUPkc");
        strangers.setName("Strangers");
        strangers.setPopularity(66);
        strangers.setPreviewUrl(null);
        strangers.setTrackNumber(1);
        strangers.setType();
        strangers.setUri("spotify:track:3fJaqjV813edLN5wrxUPkc");
    }

    @Test
    public void mapSongCorrectly() {


    }

    @Test
    public void mapSimpleArtistCorrectly() {

    }

    @Test
    public void mapArtistCorrectly() {

    }

    @Test
    public void mapUserCorrectly() {

    }

    @Test
    public void mapPlaylistCorrectly() {

    }
}
