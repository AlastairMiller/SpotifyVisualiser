package finalyearproject;

import com.wrapper.spotify.models.*;
import finalyearproject.model.Song;
import finalyearproject.utilities.DownstreamMapper;
import finalyearproject.utilities.ImageBuilder;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@Data
public class MapperTest {

    Track strangers = new Track();
    SimpleArtist simpleSigrid = new SimpleArtist();
    Artist fullSigrid = new Artist();


    @Before
    public void before() {

        final SimpleAlbum strangersAlbum = new SimpleAlbum();
        strangersAlbum.setAlbumType(AlbumType.ALBUM);
        strangersAlbum.setExternalUrls(new ExternalUrls());
        strangersAlbum.setHref("https://api.spotify.com/v1/albums/2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setId("2zlcfXL0DThG4tcEzAIJOl");
        strangersAlbum.setImages(Arrays.asList(
                ImageBuilder.getNewImage(640, 640, "https://i.scdn.co/image/0725788b0ec7a6b2e35df5816780293f13acebef"),
                ImageBuilder.getNewImage(300, 300, "https://i.scdn.co/image/7ad46eb909c9d472e8f9c02be988b4c00e2d2f9b"),
                ImageBuilder.getNewImage(64, 64, "https://i.scdn.co/image/226910565e05223d962953b224f0d44133b978d0"))
        );

        strangersAlbum.setType(SpotifyEntityType.ALBUM);
        strangersAlbum.setUri("spotify:album:2zlcfXL0DThG4tcEzAIJOl");
        ExternalUrls albumExternalUrl = new ExternalUrls();
        Map<String, String> temp = Collections.singletonMap("spotify", "https://open.spotify.com/artist/4TrraAsitQKl821DQY42cZ");
        ReflectionTestUtils.setField(albumExternalUrl, "externalUrls", temp);

        simpleSigrid.setHref("https://api.spotify.com/v1/artists/4TrraAsitQKl821DQY42cZ");
        simpleSigrid.setId("4TrraAsitQKl821DQY42cZ");
        simpleSigrid.setName("Sigrid");
        simpleSigrid.setType(SpotifyEntityType.ARTIST);
        simpleSigrid.setUri("spotify:artist:4TrraAsitQKl821DQY42cZ");
        ExternalUrls artistExternalUrl = new ExternalUrls();
        Map<String, String> temp2 = Collections.singletonMap("spotify", "https://open.spotify.com/artist/4TrraAsitQKl821DQY42cZ");
        ReflectionTestUtils.setField(artistExternalUrl, "externalUrls", temp2);
        simpleSigrid.setExternalUrls(artistExternalUrl);


        fullSigrid.setExternalUrls(artistExternalUrl);
        fullSigrid.setFollowers(new Followers(){{
            setHref(null);
            setTotal(54208);
        }});
        fullSigrid.setGenres(Arrays.asList("norwegian indie", "pop", "tropical house"));
        fullSigrid.setHref("https://api.spotify.com/v1/artists/4TrraAsitQKl821DQY42cZ");
        fullSigrid.setId("4TrraAsitQKl821DQY42cZ");
        fullSigrid.setName("Sigrid");
        fullSigrid.setPopularity(73);
        fullSigrid.setType(SpotifyEntityType.ARTIST);
        fullSigrid.setUri("spotify:artist:4TrraAsitQKl821DQY42cZ");

        strangers.setAlbum(strangersAlbum);
        strangers.setArtists(new ArrayList<SimpleArtist>(Collections.singletonList(simpleSigrid)));
        strangers.setDiscNumber(1);
        strangers.setDuration(233725);
        strangers.setExplicit(false);
        strangers.setHref("https://api.spotify.com/v1/tracks/3fJaqjV813edLN5wrxUPkc");
        strangers.setId("3fJaqjV813edLN5wrxUPkc");
        strangers.setName("Strangers");
        strangers.setPopularity(66);
        strangers.setPreviewUrl("https://p.scdn.co/mp3-preview/0ef266816cbf16433bf8617c40d4187121abc391?cid=8897482848704f2a8f8d7c79726a70d4");
        strangers.setTrackNumber(1);
        strangers.setType(SpotifyEntityType.ALBUM);
        strangers.setUri("spotify:track:3fJaqjV813edLN5wrxUPkc");

        ExternalUrls songExternalUrl = new ExternalUrls();
        Map<String, String> temp3 = Collections.singletonMap("spotify", "https://open.spotify.com/track/3fJaqjV813edLN5wrxUPkc");

        ReflectionTestUtils.setField(songExternalUrl, "externalUrls", temp3);
        strangers.setExternalUrls(songExternalUrl);
    }

    @Test
    public void mapSongCorrectly() throws MalformedURLException {
        Song mappedSong = DownstreamMapper.mapSong(strangers);
        assertEquals(strangers.getName(), mappedSong.getName());
        assertEquals(strangers.getArtists().get(0).getId(), mappedSong.getArtists().get(0).getId());
        assertEquals(strangers.getAvailableMarkets(), mappedSong.getAvailableMarkets());
        assertEquals(strangers.getDiscNumber(), mappedSong.getDiscNum());
        assertEquals(strangers.getDuration(), mappedSong.getDurationMs());
        assertEquals(strangers.getExternalUrls().get("spotify"), mappedSong.getExternalURL().toString());
        assertEquals(strangers.getHref(), mappedSong.getHref());
        assertEquals(strangers.getId(), mappedSong.getId());
        assertEquals(strangers.getPopularity(), mappedSong.getPopularity());
        assertEquals(strangers.getPreviewUrl(), mappedSong.getPreviewURL().toString());
        assertEquals(strangers.getTrackNumber(), mappedSong.getTrackNumber());
        assertEquals(strangers.getUri(), mappedSong.getUri().toString());
        assertEquals(strangers.getExternalUrls().get("spotify"), mappedSong.getExternalURL().toString());
    }

    @Test
    public void mapSimpleArtistCorrectly() throws MalformedURLException {
        finalyearproject.model.Artist mappedArtist = DownstreamMapper.mapSimpleArtist(simpleSigrid);
        assertEquals(simpleSigrid.getHref(),mappedArtist.getHref());
        assertEquals(simpleSigrid.getId(),mappedArtist.getId());
        assertEquals(simpleSigrid.getName(),mappedArtist.getName());
        assertEquals(simpleSigrid.getType().toString().toLowerCase(), mappedArtist.getType());
        assertEquals(simpleSigrid.getUri(), mappedArtist.getUri().toString());
        assertEquals(simpleSigrid.getExternalUrls().get("spotify"), mappedArtist.getExternalURL().toString());
    }

    @Test
    public void mapArtistCorrectly() throws MalformedURLException {
        finalyearproject.model.Artist mappedArtist = DownstreamMapper.mapArtist(fullSigrid);
        assertEquals(fullSigrid.getExternalUrls().get("spotify"), mappedArtist.getExternalURL().toString());
        assertEquals(fullSigrid.getGenres(), mappedArtist.getGenres());
        assertEquals(fullSigrid.getHref(), mappedArtist.getHref());
        assertEquals(fullSigrid.getId(), mappedArtist.getId());
        assertEquals(fullSigrid.getName(), mappedArtist.getName());
        assertEquals(fullSigrid.getType().toString().toLowerCase(), mappedArtist.getType());
        assertEquals(fullSigrid.getUri(), mappedArtist.getUri().toString());
        assertEquals(fullSigrid.getFollowers().getTotal(), mappedArtist.getFollowers());
        assertEquals(fullSigrid.getPopularity(), mappedArtist.getPopularity());
    }
}
