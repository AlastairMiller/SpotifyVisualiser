package finalyearproject;

import com.wrapper.spotify.models.Track;
import finalyearproject.model.Artist;
import finalyearproject.model.Song;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class DownstreamMapper {

    public static Song mapSong(Track fullTrack) throws MalformedURLException {
        List<Artist> minimisedArtists = null;
        for(int i=0; i<fullTrack.getArtists().size(); i++){
            minimisedArtists.add(mapSimpleArtist(fullTrack.getArtists().get(i)));
        }
        Song newSong = Song.builder()
                .id(fullTrack.getId())
                .artists(minimisedArtists)
                .availableMarkets(fullTrack.getAvailableMarkets())
                .discNum(fullTrack.getDiscNumber())
                .durationMs(fullTrack.getDuration())
                .explicit(fullTrack.isExplicit())
                .externalURL((new URL(fullTrack.getExternalUrls().get("1"))))
                .href(fullTrack.getHref())
                .name(fullTrack.getName())
                .previewURL(new URL(fullTrack.getPreviewUrl()))
                .trackNumber(fullTrack.getTrackNumber())
                .popularity(fullTrack.getPopularity())
                .uri(fullTrack.getUri())
                .build();
        return newSong;
    }

    public static Artist mapSimpleArtist(com.wrapper.spotify.models.SimpleArtist fullArtist) throws MalformedURLException {
        return Artist.builder()
                .id(fullArtist.getId())
                .externalURL((new URL(fullArtist.getExternalUrls().get("1"))))
                .href(fullArtist.getHref())
                .name(fullArtist.getName())
                .type(fullArtist.getType().getType())
                .uri(URI.create(fullArtist.getUri()))
                .build();
    }
    public static Artist mapArtist(com.wrapper.spotify.models.Artist fullArtist) throws MalformedURLException {
        return Artist.builder()
                .id(fullArtist.getId())
                .externalURL((new URL(fullArtist.getExternalUrls().get("1"))))
                .genres(fullArtist.getGenres())
                .href(fullArtist.getHref())
                .name(fullArtist.getName())
                .type(fullArtist.getType().getType())
                .uri(URI.create(fullArtist.getUri()))
                .build();
    }

}
