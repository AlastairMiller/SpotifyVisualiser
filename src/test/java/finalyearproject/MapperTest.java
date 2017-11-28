package finalyearproject;

import com.wrapper.spotify.models.*;
import finalyearproject.model.Song;
import finalyearproject.utilities.DownstreamMapper;
import finalyearproject.utilities.ExternalUrlBuilder;
import finalyearproject.utilities.ImageBuilder;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


@Data
public class MapperTest {

    Track strangers = new Track();
    SimpleArtist simpleSigrid = new SimpleArtist();
    Artist fullSigrid = new Artist();


    @Before
    public void before() {

        SimpleAlbum strangersAlbum = new SimpleAlbum();
        strangersAlbum.setAlbumType(AlbumType.ALBUM);
        strangersAlbum.setExternalUrls(new ExternalUrls());
        strangersAlbum.setHref("https://api.spotify.com/v1/albums/2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setId("2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setImages(new ArrayList<Image>(Arrays.asList(
                ImageBuilder.getNewImage(640, 640, "https://i.scdn.co/image/0725788b0ec7a6b2e35df5816780293f13acebef"),
                ImageBuilder.getNewImage(300, 300, "https://i.scdn.co/image/7ad46eb909c9d472e8f9c02be988b4c00e2d2f9b"),
                ImageBuilder.getNewImage(64, 64, "https://i.scdn.co/image/226910565e05223d962953b224f0d44133b978d0")))
        );
        strangersAlbum.setType(SpotifyEntityType.ALBUM);
        strangersAlbum.setUri("spotify:album:2zlcfXL0DThG4tcEzAIJOl");
        simpleSigrid.setExternalUrls(ExternalUrlBuilder.getNewExternalUrls(new HashMap<String, String>(){{
            put("spotify","https://open.spotify.com/album/2zlcfXL0DThG4tcEzAIJOl");
        }}));

        simpleSigrid.setHref("https://api.spotify.com/v1/artists/4TrraAsitQKl821DQY42cZ");
        simpleSigrid.setId("4TrraAsitQKl821DQY42cZ");
        simpleSigrid.setName("Sigrid");
        simpleSigrid.setType(SpotifyEntityType.ARTIST);
        simpleSigrid.setUri("spotify:artist:4TrraAsitQKl821DQY42cZ");
        simpleSigrid.setExternalUrls(ExternalUrlBuilder.getNewExternalUrls(new HashMap<String, String>(){{
            put("spotify","https://open.spotify.com/artist/4TrraAsitQKl821DQY42cZ");
        }}));

        // TODO use Spring Inflection test Field injection

        strangers.setAlbum(strangersAlbum);
        strangers.setArtists(new ArrayList<SimpleArtist>(Collections.singletonList(simpleSigrid)));
        strangers.setDiscNumber(1);
        strangers.setDuration(233725);
        strangers.setExplicit(false);
        strangers.setHref("https://api.spotify.com/v1/tracks/3fJaqjV813edLN5wrxUPkc");
        strangers.setId("3fJaqjV813edLN5wrxUPkc");
        strangers.setName("Strangers");
        strangers.setPopularity(66);
        strangers.setPreviewUrl(null);
        strangers.setTrackNumber(1);
        strangers.setType(SpotifyEntityType.ALBUM);
        strangers.setUri("spotify:track:3fJaqjV813edLN5wrxUPkc");
    }

    @Test
    public void mapSongCorrectly() throws MalformedURLException {
        Song mappedsong = DownstreamMapper.mapSong(strangers);
        assertSame(mappedsong,strangers);


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
