package finalyearproject.utilities;

import com.wrapper.spotify.models.Track;
import finalyearproject.model.Artist;
import finalyearproject.model.Playlist;
import finalyearproject.model.Song;
import finalyearproject.model.User;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownstreamMapper {

    public static Song mapSong(Track fullTrack) {
        List<Artist> minimisedArtists = new ArrayList<Artist>();
        for (int i = 0; i < fullTrack.getArtists().size(); i++) {
            minimisedArtists.add(mapSimpleArtist(fullTrack.getArtists().get(i)));
        }
        try {
            return Song.builder()
                    .id(fullTrack.getId())
                    .artists(minimisedArtists)
                    .availableMarkets(fullTrack.getAvailableMarkets())
                    .discNum(fullTrack.getDiscNumber())
                    .durationMs(fullTrack.getDuration())
                    .explicit(fullTrack.isExplicit())
                    .externalURL((new URL(fullTrack.getExternalUrls().get("spotify"))))
                    .href(fullTrack.getHref())
                    .name(fullTrack.getName())
                    .previewURL(new URL(fullTrack.getPreviewUrl()))
                    .trackNumber(fullTrack.getTrackNumber())
                    .popularity(fullTrack.getPopularity())
                    .uri(URI.create(fullTrack.getUri()))
                    .build();
        } catch (MalformedURLException e) {
            return Song.builder()
                    .id(fullTrack.getId())
                    .artists(minimisedArtists)
                    .availableMarkets(fullTrack.getAvailableMarkets())
                    .discNum(fullTrack.getDiscNumber())
                    .durationMs(fullTrack.getDuration())
                    .explicit(fullTrack.isExplicit())
                    .href(fullTrack.getHref())
                    .name(fullTrack.getName())
                    .trackNumber(fullTrack.getTrackNumber())
                    .popularity(fullTrack.getPopularity())
                    .uri(URI.create(fullTrack.getUri()))
                    .build();
        }
    }

    public static Artist mapSimpleArtist(com.wrapper.spotify.models.SimpleArtist fullArtist) {
        try {
            return Artist.builder()
                    .id(fullArtist.getId())
                    .externalURL((new URL(fullArtist.getExternalUrls().get("spotify"))))
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .build();
        } catch (MalformedURLException e) {
            return Artist.builder()
                    .id(fullArtist.getId())
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .build();
        }
    }

    public static Artist mapArtist(com.wrapper.spotify.models.Artist fullArtist) {
        try {
            return Artist.builder()
                    .id(fullArtist.getId())
                    .externalURL((new URL(fullArtist.getExternalUrls().get("spotify"))))
                    .genres(fullArtist.getGenres())
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .followers(fullArtist.getFollowers().getTotal())
                    .popularity(fullArtist.getPopularity())
                    .build();
        } catch (MalformedURLException e) {
            return Artist.builder()
                    .id(fullArtist.getId())
                    .genres(fullArtist.getGenres())
                    .href(fullArtist.getHref())
                    .name(fullArtist.getName())
                    .type(fullArtist.getType().getType())
                    .uri(URI.create(fullArtist.getUri()))
                    .followers(fullArtist.getFollowers().getTotal())
                    .popularity(fullArtist.getPopularity())
                    .build();
        }
    }

    public static User mapUser(com.wrapper.spotify.models.User fullUser) {
        List<String> imageUrls = new ArrayList<String>();
        if( fullUser.getImages()!=null) {
            for (int i = 0; i < fullUser.getImages().size(); i++) {
                imageUrls.add(fullUser.getImages().get(i).getUrl());
            }
        }
        return User.builder()
                .id(fullUser.getId())
                .uri(URI.create(fullUser.getUri()))
                .displayName(fullUser.getDisplayName())
                .numOfFollowers(fullUser.getFollowers().getTotal())
                .href(fullUser.getHref())
                .images(imageUrls)
                .build();
    }

    public static Playlist mapPlaylist(com.wrapper.spotify.models.Playlist fullPlaylist){
        List<String> imageUrls = new ArrayList<String>();
        if(fullPlaylist.getImages()!=null) {
            for (int i = 0; i < fullPlaylist.getImages().size(); i++) {
                imageUrls.add(fullPlaylist.getImages().get(i).getUrl());
            }
        }
        List<Song> formattedSongs = new ArrayList<Song>();
        for (int n = 0; n < fullPlaylist.getTracks().getItems().size(); n++) {
            formattedSongs.add(mapSong(fullPlaylist.getTracks().getItems().get(n).getTrack()));
        }
        try {
            return Playlist.builder()
                    .id(fullPlaylist.getId())
                    .uri(URI.create(fullPlaylist.getUri()))
                    .externalURL(new URL(fullPlaylist.getExternalUrls().get("spotify")))
                    .numOfFollowers(fullPlaylist.getFollowers().getTotal())
                    .href(fullPlaylist.getHref())
                    .images(imageUrls)
                    .name(fullPlaylist.getName())
                    .owner(mapUser(fullPlaylist.getOwner()))
                    .tracks(formattedSongs)
                    .build();
        } catch (MalformedURLException e) {
            return Playlist.builder()
                    .id(fullPlaylist.getId())
                    .uri(URI.create(fullPlaylist.getUri()))
                    .numOfFollowers(fullPlaylist.getFollowers().getTotal())
                    .href(fullPlaylist.getHref())
                    .images(imageUrls)
                    .name(fullPlaylist.getName())
                    .owner(mapUser(fullPlaylist.getOwner()))
                    .tracks(formattedSongs)
                    .build();
        }

    }


}
